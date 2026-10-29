package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VehicleInput
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.AppStrings
import com.example.ui.theme.DiagnosticAmber
import com.example.ui.theme.ElectricBlueLight
import com.example.ui.theme.ElectricBluePrimary
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCardBackground
import com.example.ui.theme.NavyCardElevated
import com.example.ui.theme.NavyDarkBackground
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    initialCode: String = "",
    strings: AppStrings,
    currentLanguage: AppLanguage,
    isDiagnosing: Boolean,
    diagnosisError: String?,
    onRunDiagnostic: (VehicleInput) -> Unit,
    modifier: Modifier = Modifier
) {
    var faultCode by remember { mutableStateOf(initialCode) }
    var make by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var selectedFuel by remember { mutableStateOf("Petrol") }
    var engineCapacity by remember { mutableStateOf("") }
    var liveSensorData by remember { mutableStateOf("") }
    var technicianNotes by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

    val fuelOptions = listOf(
        Pair("Petrol", strings.petrol),
        Pair("Diesel", strings.diesel),
        Pair("CNG", strings.cng),
        Pair("Hybrid", strings.hybrid),
        Pair("Electric", strings.electric)
    )

    val commonSymptomsList = listOf(
        "Check Engine Light Flashing",
        "Engine Shake / Rough Idle",
        "Loss of Power / Hesitation",
        "Long Crank / Hard Starting",
        "Engine Stalls Under Acceleration",
        "Black Exhaust Smoke",
        "White Exhaust Smoke",
        "Engine Overheating",
        "Hissing / Turbo Whistle",
        "ABS / Traction Light On"
    )

    val selectedSymptoms = remember { mutableStateListOf<String>() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDarkBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
    ) {
        // 1. Fault Code Input Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyCardElevated),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, ElectricBluePrimary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(ElectricBluePrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("1", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            text = strings.searchFaultCode,
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedTextField(
                        value = faultCode,
                        onValueChange = {
                            faultCode = it.uppercase().trim()
                            validationError = null
                        },
                        placeholder = { Text(strings.searchCodePlaceholder, color = TextTertiary) },
                        singleLine = true,
                        isError = validationError != null,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("diagnostic_fault_code_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlueLight,
                            unfocusedBorderColor = NavyBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = NavyDarkBackground,
                            unfocusedContainerColor = NavyDarkBackground
                        )
                    )

                    if (validationError != null) {
                        Text(
                            text = validationError ?: "",
                            color = androidx.compose.ui.graphics.Color(0xFFFF4D6D),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // 2. Vehicle Specification Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(NavyCardElevated),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("2", color = ElectricBlueLight, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            text = strings.vehicleDetails,
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = make,
                            onValueChange = { make = it },
                            label = { Text(strings.make, fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f).testTag("vehicle_make_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricBluePrimary,
                                unfocusedBorderColor = NavyBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )

                        OutlinedTextField(
                            value = model,
                            onValueChange = { model = it },
                            label = { Text(strings.model, fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f).testTag("vehicle_model_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricBluePrimary,
                                unfocusedBorderColor = NavyBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = year,
                            onValueChange = { year = it },
                            label = { Text(strings.year, fontSize = 11.sp) },
                            placeholder = { Text("e.g. 2021", color = TextTertiary) },
                            singleLine = true,
                            modifier = Modifier.weight(1f).testTag("vehicle_year_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricBluePrimary,
                                unfocusedBorderColor = NavyBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )

                        OutlinedTextField(
                            value = engineCapacity,
                            onValueChange = { engineCapacity = it },
                            label = { Text("Engine (e.g. 1.5L / 2.0L)", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f).testTag("vehicle_engine_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricBluePrimary,
                                unfocusedBorderColor = NavyBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }

                    // Fuel Type Chips
                    Text(
                        text = strings.fuelType,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        fuelOptions.forEach { (fuelKey, fuelLabel) ->
                            val isSelected = selectedFuel == fuelKey
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedFuel = fuelKey },
                                label = { Text(fuelLabel, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = ElectricBluePrimary,
                                    selectedLabelColor = TextPrimary,
                                    containerColor = NavyDarkBackground,
                                    labelColor = TextSecondary
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    borderColor = if (isSelected) ElectricBlueLight else NavyBorder,
                                    enabled = true,
                                    selected = isSelected
                                ),
                                modifier = Modifier.testTag("fuel_chip_$fuelKey")
                            )
                        }
                    }
                }
            }
        }

        // 3. Observed Symptoms Checklist
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = strings.symptoms,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        commonSymptomsList.forEach { symptom ->
                            val isChecked = selectedSymptoms.contains(symptom)
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isChecked) ElectricBluePrimary.copy(alpha = 0.2f) else NavyDarkBackground,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isChecked) ElectricBluePrimary else NavyBorder
                                ),
                                modifier = Modifier
                                    .clickable {
                                        if (isChecked) selectedSymptoms.remove(symptom)
                                        else selectedSymptoms.add(symptom)
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    if (isChecked) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = ElectricBlueLight,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                    Text(
                                        text = symptom,
                                        color = if (isChecked) ElectricBlueLight else TextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. Live Sensor Data & Technician Notes
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = strings.liveSensorData,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = liveSensorData,
                        onValueChange = { liveSensorData = it },
                        placeholder = { Text("e.g. MAF 1.1 g/s idle, STFT +22%, FRP 290 bar", color = TextTertiary, fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("live_sensor_input"),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBluePrimary,
                            unfocusedBorderColor = NavyBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    Text(
                        text = strings.additionalNotes,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = technicianNotes,
                        onValueChange = { technicianNotes = it },
                        placeholder = { Text("e.g. Spark plugs replaced yesterday; misfire still persists on cylinder 3.", color = TextTertiary, fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("technician_notes_input"),
                        minLines = 2,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBluePrimary,
                            unfocusedBorderColor = NavyBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                }
            }
        }

        if (diagnosisError != null) {
            item {
                Surface(
                    color = androidx.compose.ui.graphics.Color(0x33FF4D6D),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, androidx.compose.ui.graphics.Color(0xFFFF4D6D))
                ) {
                    Text(
                        text = diagnosisError,
                        color = androidx.compose.ui.graphics.Color(0xFFFFB3C1),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        // 5. Submit Diagnostic Button
        item {
            Button(
                onClick = {
                    if (faultCode.isBlank()) {
                        validationError = strings.enterCodeError
                    } else {
                        onRunDiagnostic(
                            VehicleInput(
                                faultCode = faultCode,
                                make = make,
                                model = model,
                                year = year,
                                fuelType = selectedFuel,
                                engineCapacity = engineCapacity,
                                selectedSymptoms = selectedSymptoms.toList(),
                                liveSensorData = liveSensorData,
                                technicianNotes = technicianNotes
                            )
                        )
                    }
                },
                enabled = !isDiagnosing,
                colors = ButtonDefaults.buttonColors(containerColor = ElectricBluePrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("run_diagnosis_submit_button")
            ) {
                if (isDiagnosing) {
                    CircularProgressIndicator(
                        color = TextPrimary,
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.5.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(strings.analyzingWithAI, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                } else {
                    Icon(imageVector = Icons.Default.Speed, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(strings.startDiagnosis, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
