package com.example.data.model

enum class SeverityLevel(val labelEn: String, val labelHi: String) {
    LOW("Minor / Informational", "सामान्य / सूचनात्मक"),
    MEDIUM("Moderate / Attention Needed", "मध्यम / ध्यान देने योग्य"),
    HIGH("High / Performance Affected", "गंभीर / प्रदर्शन प्रभावित"),
    CRITICAL("Critical / Immediate Stop Risk", "अत्यंत गंभीर / इंजन क्षति का खतरा")
}

data class SensorCheckItem(
    val component: String,
    val wireOrPin: String,
    val testMethod: String,
    val normalRange: String
)

data class ExpectedValueItem(
    val parameter: String,
    val idleSpec: String,
    val loadSpec: String,
    val unit: String
)

data class RepairItem(
    val action: String,
    val probability: String, // High, Medium, Low
    val difficulty: String // Easy, Moderate, Advanced
)

data class DiagnosticResult(
    val faultCode: String,
    val title: String,
    val severity: SeverityLevel,
    val systemInvolved: String,
    val summary: String,
    val symptoms: List<String>,
    val possibleCauses: List<String>,
    val diagnosticProcedure: List<String>,
    val sensorChecks: List<SensorCheckItem>,
    val expectedValues: List<ExpectedValueItem>,
    val possibleRepairs: List<RepairItem>,
    val toolsRequired: List<String>,
    val safetyPrecautions: List<String>,
    val isGeneric: Boolean = true,
    val vehicleSpecificNotes: String = ""
)

data class VehicleInput(
    val faultCode: String,
    val make: String = "",
    val model: String = "",
    val year: String = "",
    val fuelType: String = "Petrol",
    val engineCapacity: String = "",
    val selectedSymptoms: List<String> = emptyList(),
    val liveSensorData: String = "",
    val technicianNotes: String = ""
)
