package com.example.data.repository

import android.util.Log
import com.example.BuildConfig
import com.example.data.automotive.AutomotiveKnowledgeBase
import com.example.data.local.DiagnosticDao
import com.example.data.local.DiagnosticRecord
import com.example.data.model.DiagnosticResult
import com.example.data.model.ExpectedValueItem
import com.example.data.model.RepairItem
import com.example.data.model.SensorCheckItem
import com.example.data.model.SeverityLevel
import com.example.data.model.VehicleInput
import com.example.data.remote.GeminiApiService
import com.example.data.remote.GeminiContent
import com.example.data.remote.GeminiGenerationConfig
import com.example.data.remote.GeminiPart
import com.example.data.remote.GeminiRequest
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import org.json.JSONObject

class DiagnosticRepository(
    private val dao: DiagnosticDao,
    private val apiService: GeminiApiService
) {
    private val tag = "DiagnosticRepo"

    val allRecords: Flow<List<DiagnosticRecord>> = dao.getAllRecords()
    val bookmarkedRecords: Flow<List<DiagnosticRecord>> = dao.getBookmarkedRecords()

    fun getRecentRecords(limit: Int = 10): Flow<List<DiagnosticRecord>> = dao.getRecentRecords(limit)

    fun searchRecords(query: String): Flow<List<DiagnosticRecord>> = dao.searchRecords(query)

    suspend fun getRecordById(id: Long): DiagnosticRecord? = dao.getRecordById(id)

    suspend fun updateRepairNotes(id: Long, notes: String) {
        dao.updateRepairNotes(id, notes)
    }

    suspend fun toggleBookmark(id: Long, current: Boolean) {
        dao.setBookmark(id, !current)
    }

    suspend fun deleteRecord(id: Long) {
        dao.deleteRecordById(id)
    }

    suspend fun clearAllRecords() {
        dao.clearAllRecords()
    }

    suspend fun runDiagnosis(input: VehicleInput, customApiKey: String?): Pair<DiagnosticResult, Long> {
        val apiKey = when {
            !customApiKey.isNullOrBlank() -> customApiKey
            try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }.isNotBlank() &&
                BuildConfig.GEMINI_API_KEY != "MY_GEMINI_API_KEY" -> BuildConfig.GEMINI_API_KEY
            else -> ""
        }

        var result: DiagnosticResult? = null

        if (apiKey.isNotBlank()) {
            try {
                result = fetchFromGemini(apiKey, input)
            } catch (e: Exception) {
                Log.w(tag, "Gemini API request failed, falling back to expert automotive knowledge base: ${e.message}")
            }
        }

        if (result == null) {
            result = AutomotiveKnowledgeBase.getDiagnostic(input)
        }

        val jsonString = serializeToJson(result)
        val record = DiagnosticRecord(
            faultCode = result.faultCode,
            vehicleMake = input.make,
            vehicleModel = input.model,
            modelYear = input.year,
            fuelType = input.fuelType,
            engineDetails = input.engineCapacity,
            userSymptoms = input.selectedSymptoms.joinToString(", "),
            liveSensorData = input.liveSensorData,
            diagnosticTitle = result.title,
            diagnosticSummary = result.summary,
            severityString = result.severity.name,
            systemInvolved = result.systemInvolved,
            fullResultJson = jsonString,
            userRepairNotes = input.technicianNotes,
            isBookmarked = false
        )

        val insertedId = dao.insertRecord(record)
        return Pair(result, insertedId)
    }

    private suspend fun fetchFromGemini(apiKey: String, input: VehicleInput): DiagnosticResult? {
        val prompt = """
            You are a Master Automotive Diagnostic Engineer and Technical Service Specialist.
            Analyze this automotive vehicle fault:
            Fault Code: ${input.faultCode}
            Vehicle: ${input.year} ${input.make} ${input.model}
            Fuel Type: ${input.fuelType}
            Engine Capacity: ${input.engineCapacity}
            Observed Symptoms: ${input.selectedSymptoms.joinToString(", ")}
            Live Sensor Readings: ${input.liveSensorData}
            Technician Notes: ${input.technicianNotes}

            Rules for accuracy:
            1. Never claim a component is definitely defective based on a fault code alone; specify testing first.
            2. Distinguish generic OBD-II vs OEM vehicle-specific info.
            3. Provide realistic multimeter voltage, resistance, or frequency test values.
            4. Output ONLY valid JSON in this exact structure without markdown backticks:
            {
              "faultCode": "${input.faultCode.uppercase()}",
              "title": "Clear Technical Title",
              "severity": "LOW" | "MEDIUM" | "HIGH" | "CRITICAL",
              "systemInvolved": "Primary systems involved",
              "summary": "Engineering summary of the fault mechanism",
              "symptoms": ["Symptom 1", "Symptom 2"],
              "possibleCauses": ["Cause 1", "Cause 2"],
              "diagnosticProcedure": ["Step 1: ...", "Step 2: ..."],
              "sensorChecks": [
                {
                  "component": "Sensor/Relay name",
                  "wireOrPin": "Pinout/Wire info",
                  "testMethod": "Multimeter / Scope test method",
                  "normalRange": "Expected values"
                }
              ],
              "expectedValues": [
                {
                  "parameter": "Parameter name",
                  "idleSpec": "Idle value",
                  "loadSpec": "Load value",
                  "unit": "Unit"
                }
              ],
              "possibleRepairs": [
                {
                  "action": "Repair action",
                  "probability": "High" | "Medium" | "Low",
                  "difficulty": "Easy" | "Moderate" | "Advanced"
                }
              ],
              "toolsRequired": ["Tool 1", "Tool 2"],
              "safetyPrecautions": ["Safety 1", "Safety 2"],
              "isGeneric": true,
              "vehicleSpecificNotes": "Vehicle-specific notes if applicable"
            }
        """.trimIndent()

        val request = GeminiRequest(
            contents = listOf(
                GeminiContent(
                    parts = listOf(GeminiPart(text = prompt))
                )
            ),
            generationConfig = GeminiGenerationConfig(temperature = 0.2, responseMimeType = "application/json")
        )

        val response = apiService.generateContent(apiKey, request)
        val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: return null
        return parseFromJson(text, input.faultCode)
    }

    private fun serializeToJson(result: DiagnosticResult): String {
        val root = JSONObject()
        root.put("faultCode", result.faultCode)
        root.put("title", result.title)
        root.put("severity", result.severity.name)
        root.put("systemInvolved", result.systemInvolved)
        root.put("summary", result.summary)
        root.put("symptoms", JSONArray(result.symptoms))
        root.put("possibleCauses", JSONArray(result.possibleCauses))
        root.put("diagnosticProcedure", JSONArray(result.diagnosticProcedure))

        val sensorsArr = JSONArray()
        result.sensorChecks.forEach {
            val item = JSONObject()
            item.put("component", it.component)
            item.put("wireOrPin", it.wireOrPin)
            item.put("testMethod", it.testMethod)
            item.put("normalRange", it.normalRange)
            sensorsArr.put(item)
        }
        root.put("sensorChecks", sensorsArr)

        val valuesArr = JSONArray()
        result.expectedValues.forEach {
            val item = JSONObject()
            item.put("parameter", it.parameter)
            item.put("idleSpec", it.idleSpec)
            item.put("loadSpec", it.loadSpec)
            item.put("unit", it.unit)
            valuesArr.put(item)
        }
        root.put("expectedValues", valuesArr)

        val repairsArr = JSONArray()
        result.possibleRepairs.forEach {
            val item = JSONObject()
            item.put("action", it.action)
            item.put("probability", it.probability)
            item.put("difficulty", it.difficulty)
            repairsArr.put(item)
        }
        root.put("possibleRepairs", repairsArr)

        root.put("toolsRequired", JSONArray(result.toolsRequired))
        root.put("safetyPrecautions", JSONArray(result.safetyPrecautions))
        root.put("isGeneric", result.isGeneric)
        root.put("vehicleSpecificNotes", result.vehicleSpecificNotes)

        return root.toString()
    }

    fun parseFromJson(jsonString: String, fallbackCode: String): DiagnosticResult {
        return try {
            val cleanJson = jsonString.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
            val root = JSONObject(cleanJson)

            val code = root.optString("faultCode", fallbackCode)
            val title = root.optString("title", "Diagnostic Analysis for $code")
            val severityStr = root.optString("severity", "MEDIUM")
            val severity = try { SeverityLevel.valueOf(severityStr) } catch (e: Exception) { SeverityLevel.MEDIUM }
            val system = root.optString("systemInvolved", "Engine & Powertrain")
            val summary = root.optString("summary", "Diagnostic report generated for $code.")

            val symptoms = root.optJSONArray("symptoms")?.let { arr ->
                (0 until arr.length()).map { arr.getString(it) }
            } ?: emptyList()

            val causes = root.optJSONArray("possibleCauses")?.let { arr ->
                (0 until arr.length()).map { arr.getString(it) }
            } ?: emptyList()

            val procedure = root.optJSONArray("diagnosticProcedure")?.let { arr ->
                (0 until arr.length()).map { arr.getString(it) }
            } ?: emptyList()

            val sensors = mutableListOf<SensorCheckItem>()
            root.optJSONArray("sensorChecks")?.let { arr ->
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    sensors.add(
                        SensorCheckItem(
                            component = obj.optString("component", ""),
                            wireOrPin = obj.optString("wireOrPin", ""),
                            testMethod = obj.optString("testMethod", ""),
                            normalRange = obj.optString("normalRange", "")
                        )
                    )
                }
            }

            val expected = mutableListOf<ExpectedValueItem>()
            root.optJSONArray("expectedValues")?.let { arr ->
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    expected.add(
                        ExpectedValueItem(
                            parameter = obj.optString("parameter", ""),
                            idleSpec = obj.optString("idleSpec", ""),
                            loadSpec = obj.optString("loadSpec", ""),
                            unit = obj.optString("unit", "")
                        )
                    )
                }
            }

            val repairs = mutableListOf<RepairItem>()
            root.optJSONArray("possibleRepairs")?.let { arr ->
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    repairs.add(
                        RepairItem(
                            action = obj.optString("action", ""),
                            probability = obj.optString("probability", "Medium"),
                            difficulty = obj.optString("difficulty", "Moderate")
                        )
                    )
                }
            }

            val tools = root.optJSONArray("toolsRequired")?.let { arr ->
                (0 until arr.length()).map { arr.getString(it) }
            } ?: emptyList()

            val safety = root.optJSONArray("safetyPrecautions")?.let { arr ->
                (0 until arr.length()).map { arr.getString(it) }
            } ?: emptyList()

            val isGeneric = root.optBoolean("isGeneric", true)
            val vNotes = root.optString("vehicleSpecificNotes", "")

            DiagnosticResult(
                faultCode = code,
                title = title,
                severity = severity,
                systemInvolved = system,
                summary = summary,
                symptoms = symptoms,
                possibleCauses = causes,
                diagnosticProcedure = procedure,
                sensorChecks = sensors,
                expectedValues = expected,
                possibleRepairs = repairs,
                toolsRequired = tools,
                safetyPrecautions = safety,
                isGeneric = isGeneric,
                vehicleSpecificNotes = vNotes
            )
        } catch (e: Exception) {
            Log.e(tag, "Failed to parse diagnostic JSON, falling back", e)
            AutomotiveKnowledgeBase.getDiagnostic(VehicleInput(fallbackCode))
        }
    }
}
