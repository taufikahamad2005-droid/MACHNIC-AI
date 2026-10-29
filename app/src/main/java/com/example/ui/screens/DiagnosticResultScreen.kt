package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DiagnosticResult
import com.example.data.model.SeverityLevel
import com.example.ui.components.SeverityChip
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.AppStrings
import com.example.ui.theme.DiagnosticAmber
import com.example.ui.theme.DiagnosticGreen
import com.example.ui.theme.DiagnosticRed
import com.example.ui.theme.ElectricBlueLight
import com.example.ui.theme.ElectricBluePrimary
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCardBackground
import com.example.ui.theme.NavyCardElevated
import com.example.ui.theme.NavyDarkBackground
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun DiagnosticResultScreen(
    result: DiagnosticResult,
    recordId: Long,
    strings: AppStrings,
    currentLanguage: AppLanguage,
    initialNotes: String = "",
    isBookmarked: Boolean = false,
    onToggleBookmark: (Long, Boolean) -> Unit,
    onSaveNotes: (Long, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isHindi = currentLanguage == AppLanguage.HINDI

    var selectedTab by remember { mutableIntStateOf(0) }
    var notesText by remember { mutableStateOf(initialNotes) }
    var notesSavedMessage by remember { mutableStateOf(false) }
    var bookmarkedState by remember { mutableStateOf(isBookmarked) }

    val tabs = listOf(
        "Overview",
        "Procedure",
        "Multimeter Specs",
        "Sensors & Wiring",
        "Repairs & Tools",
        "My Notes"
    )

    fun shareDiagnosticReport() {
        val shareText = buildString {
            append("--- MECHANIC AI DIAGNOSTIC REPORT ---\n")
            append("Fault Code: ${result.faultCode}\n")
            append("Title: ${result.title}\n")
            append("Severity: ${result.severity.name}\n")
            append("System Involved: ${result.systemInvolved}\n\n")
            append("SUMMARY:\n${result.summary}\n\n")
            append("POSSIBLE CAUSES:\n")
            result.possibleCauses.forEachIndexed { i, c -> append("${i + 1}. $c\n") }
            append("\nDIAGNOSTIC PROCEDURE:\n")
            result.diagnosticProcedure.forEach { append("$it\n") }
            append("\nEXPECTED MULTIMETER RANGES:\n")
            result.expectedValues.forEach { append("• ${it.parameter}: Idle: ${it.idleSpec}, Load: ${it.loadSpec} (${it.unit})\n") }
            append("\nSAFETY NOTICE:\n${strings.safetyDisclaimer}\n")
            append("--- Generated by Mechanic AI ---")
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(sendIntent, "Share Diagnostic Report"))
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDarkBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
    ) {
        // 1. Fault Code Header Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyCardElevated),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, ElectricBluePrimary.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth().testTag("result_header_card")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = ElectricBluePrimary.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ElectricBluePrimary)
                        ) {
                            Text(
                                text = result.faultCode,
                                color = ElectricBlueLight,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Bookmark Button
                            IconButton(
                                onClick = {
                                    bookmarkedState = !bookmarkedState
                                    onToggleBookmark(recordId, !bookmarkedState)
                                },
                                modifier = Modifier.size(36.dp).testTag("result_bookmark_button")
                            ) {
                                Icon(
                                    imageVector = if (bookmarkedState) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "Bookmark",
                                    tint = if (bookmarkedState) DiagnosticAmber else TextSecondary
                                )
                            }

                            // Share Report Button
                            IconButton(
                                onClick = { shareDiagnosticReport() },
                                modifier = Modifier.size(36.dp).testTag("result_share_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    tint = ElectricBlueLight
                                )
                            }
                        }
                    }

                    Text(
                        text = result.title,
                        color = TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SeverityChip(severity = result.severity, isHindi = isHindi)
                        Text(
                            text = if (result.isGeneric) "Generic OBD-II Standard" else "Vehicle Specific OEM",
                            color = TextTertiary,
                            fontSize = 11.sp
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = NavyDarkBackground,
                        border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = strings.systemsInvolved,
                                color = TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = result.systemInvolved,
                                color = ElectricBlueLight,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // 2. Segmented Navigation Tabs
        item {
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = NavyDarkBackground,
                contentColor = ElectricBlueLight,
                edgePadding = 0.dp,
                divider = {}
            ) {
                tabs.forEachIndexed { index, tabName ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = tabName,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) ElectricBlueLight else TextSecondary
                            )
                        },
                        modifier = Modifier.testTag("result_tab_$index")
                    )
                }
            }
        }

        // 3. Tab Contents
        when (selectedTab) {
            0 -> {
                // Overview & Causes Tab
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("Engineering Summary", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text(text = result.summary, color = TextSecondary, fontSize = 13.sp, lineHeight = 18.sp)
                        }
                    }
                }

                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(strings.symptomsObserved, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            result.symptoms.forEach { symptom ->
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Top) {
                                    Text("•", color = ElectricBlueLight, fontWeight = FontWeight.Bold)
                                    Text(symptom, color = TextSecondary, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }

                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(strings.possibleCauses, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            result.possibleCauses.forEachIndexed { i, cause ->
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Top) {
                                    Text("${i + 1}.", color = ElectricBlueLight, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(cause, color = TextSecondary, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // Step-by-Step Procedure Tab
                item {
                    Text(
                        text = strings.stepByStepProcedure,
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                itemsIndexed(result.diagnosticProcedure) { index, step ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(ElectricBluePrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("${index + 1}", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Text(
                                text = step,
                                color = TextPrimary,
                                fontSize = 13.sp,
                                lineHeight = 18.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            2 -> {
                // Expected Multimeter Ranges Tab
                item {
                    Text(
                        text = strings.expectedRanges,
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                itemsIndexed(result.expectedValues) { _, item ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(item.parameter, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text(item.unit, color = ElectricBlueLight, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    color = NavyDarkBackground,
                                    shape = RoundedCornerShape(8.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text("Idle / Key ON", color = TextTertiary, fontSize = 10.sp)
                                        Text(item.idleSpec, color = DiagnosticGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Surface(
                                    color = NavyDarkBackground,
                                    shape = RoundedCornerShape(8.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text("Load / 2500 RPM", color = TextTertiary, fontSize = 10.sp)
                                        Text(item.loadSpec, color = ElectricBlueLight, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            3 -> {
                // Sensors & Wiring Tab
                item {
                    Text(
                        text = strings.sensorsAndWiring,
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                itemsIndexed(result.sensorChecks) { _, sensor ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(sensor.component, color = ElectricBlueLight, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("Wire / Pin: ${sensor.wireOrPin}", color = TextSecondary, fontSize = 12.sp)
                            Text("Test Method: ${sensor.testMethod}", color = TextSecondary, fontSize = 12.sp)
                            Surface(
                                color = NavyDarkBackground,
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Normal: ${sensor.normalRange}",
                                    color = DiagnosticGreen,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }

            4 -> {
                // Repairs & Tools Tab
                item {
                    Text(
                        text = strings.possibleRepairs,
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                itemsIndexed(result.possibleRepairs) { _, repair ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(repair.action, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("Difficulty: ${repair.difficulty}", color = TextSecondary, fontSize = 11.sp)
                            }
                            Surface(
                                color = if (repair.probability == "High") DiagnosticGreen.copy(alpha = 0.2f) else NavyDarkBackground,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "${repair.probability} Probability",
                                    color = if (repair.probability == "High") DiagnosticGreen else TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(strings.toolsRequired, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            result.toolsRequired.forEach { tool ->
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(Icons.Default.Build, contentDescription = null, tint = ElectricBlueLight, modifier = Modifier.size(16.dp))
                                    Text(tool, color = TextSecondary, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            5 -> {
                // My Repair Notes Tab
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(strings.myRepairNotes, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text(
                                "Add custom garage notes, parts replaced, customer vehicle symptoms, and next actions.",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )

                            OutlinedTextField(
                                value = notesText,
                                onValueChange = {
                                    notesText = it
                                    notesSavedMessage = false
                                },
                                placeholder = { Text("Write personal diagnostic notes here...", color = TextTertiary) },
                                minLines = 5,
                                modifier = Modifier.fillMaxWidth().testTag("repair_notes_text_field"),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ElectricBluePrimary,
                                    unfocusedBorderColor = NavyBorder,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary,
                                    focusedContainerColor = NavyDarkBackground,
                                    unfocusedContainerColor = NavyDarkBackground
                                )
                            )

                            Button(
                                onClick = {
                                    onSaveNotes(recordId, notesText)
                                    notesSavedMessage = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ElectricBluePrimary),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth().height(46.dp).testTag("save_repair_notes_button")
                            ) {
                                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(strings.saveNotes, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }

                            if (notesSavedMessage) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = DiagnosticGreen, modifier = Modifier.size(16.dp))
                                    Text(strings.savedSuccessfully, color = DiagnosticGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. Critical Automotive Safety Warning Banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DiagnosticRed.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = DiagnosticRed,
                        modifier = Modifier.size(22.dp)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = strings.safetyPrecautions,
                            color = DiagnosticRed,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = strings.safetyDisclaimer,
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }
    }
}
