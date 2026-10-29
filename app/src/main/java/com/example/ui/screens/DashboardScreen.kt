package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DiagnosticRecord
import com.example.data.local.TechnicianProfile
import com.example.data.model.LibraryCategory
import com.example.data.model.SeverityLevel
import com.example.data.model.VehicleInput
import com.example.ui.components.SeverityChip
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.AppStrings
import com.example.ui.theme.DiagnosticAmber
import com.example.ui.theme.DiagnosticGreen
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
fun DashboardScreen(
    strings: AppStrings,
    profile: TechnicianProfile,
    recentRecords: List<DiagnosticRecord>,
    categories: List<LibraryCategory>,
    currentLanguage: AppLanguage,
    onQuickSearch: (String) -> Unit,
    onOpenSearch: () -> Unit,
    onReopenRecord: (DiagnosticRecord) -> Unit,
    onOpenCategory: (String) -> Unit,
    onOpenHistory: () -> Unit,
    onOpenLibrary: () -> Unit,
    modifier: Modifier = Modifier
) {
    var quickCodeInput by remember { mutableStateOf("") }
    val isHindi = currentLanguage == AppLanguage.HINDI

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDarkBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 28.dp)
    ) {
        // 1. Technician Status Header
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                modifier = Modifier.fillMaxWidth().testTag("dashboard_profile_card")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(ElectricBlueLight.copy(alpha = 0.3f), ElectricBluePrimary.copy(alpha = 0.1f))
                                )
                            )
                            .border(1.5.dp, ElectricBluePrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = null,
                            tint = ElectricBlueLight,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = profile.garageName,
                            color = ElectricBlueLight,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = profile.name,
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(DiagnosticGreen)
                            )
                            Text(
                                text = strings.onlineStatus,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // 2. Quick Fault Code Diagnostic Search Box
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = NavyCardElevated
                ),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, ElectricBluePrimary.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = strings.quickOBD2Search,
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = quickCodeInput,
                        onValueChange = { quickCodeInput = it.uppercase() },
                        placeholder = { Text(strings.searchCodePlaceholder, color = TextTertiary, fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = ElectricBlueLight)
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("dashboard_quick_search_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBluePrimary,
                            unfocusedBorderColor = NavyBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = NavyDarkBackground,
                            unfocusedContainerColor = NavyDarkBackground
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                if (quickCodeInput.isNotBlank()) {
                                    onQuickSearch(quickCodeInput)
                                } else {
                                    onOpenSearch()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricBluePrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).height(44.dp).testTag("dashboard_diagnose_button")
                        ) {
                            Icon(Icons.Default.Speed, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(strings.startDiagnosis, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Quick OBD-II Fault Code Chips
                    Text(
                        text = "Common Fault Codes:",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )

                    val quickCodes = listOf("P0300", "P0171", "P0420", "P0087", "P0401", "P2002", "U0100")
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(quickCodes) { code ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = NavyCardBackground,
                                border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                                modifier = Modifier
                                    .clickable { onQuickSearch(code) }
                                    .testTag("quick_code_$code")
                            ) {
                                Text(
                                    text = code,
                                    color = ElectricBlueLight,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Recent Diagnostics Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = strings.recentSearches,
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                if (recentRecords.isNotEmpty()) {
                    Text(
                        text = "View All →",
                        color = ElectricBlueLight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clickable { onOpenHistory() }
                            .testTag("view_all_history_button")
                    )
                }
            }
        }

        if (recentRecords.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = TextTertiary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = strings.noSearchesYet,
                            color = TextSecondary,
                            fontSize = 13.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(recentRecords) { record ->
                RecentDiagnosticCard(
                    record = record,
                    isHindi = isHindi,
                    onClick = { onReopenRecord(record) }
                )
            }
        }

        // 4. Vehicle Repair Library Categories
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = strings.quickCategories,
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "All Guides →",
                    color = ElectricBlueLight,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clickable { onOpenLibrary() }
                        .testTag("view_all_library_button")
                )
            }
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(categories) { cat ->
                    LibraryCategoryChip(
                        category = cat,
                        isHindi = isHindi,
                        onClick = { onOpenCategory(cat.id) }
                    )
                }
            }
        }

        // 5. Automotive Engineering Safety Notice
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DiagnosticAmber.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = DiagnosticAmber,
                        modifier = Modifier.size(20.dp)
                    )
                    Column {
                        Text(
                            text = "Automotive Diagnostic Best Practice",
                            color = DiagnosticAmber,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = strings.safetyNotice,
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

@Composable
private fun RecentDiagnosticCard(
    record: DiagnosticRecord,
    isHindi: Boolean,
    onClick: () -> Unit
) {
    val severity = try {
        SeverityLevel.valueOf(record.severityString)
    } catch (e: Exception) {
        SeverityLevel.MEDIUM
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("recent_record_${record.id}")
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(NavyCardElevated),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = record.faultCode,
                    color = ElectricBlueLight,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = record.diagnosticTitle,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                val vehicleDesc = listOf(record.modelYear, record.vehicleMake, record.vehicleModel)
                    .filter { it.isNotBlank() }
                    .joinToString(" ")
                    .ifEmpty { "OBD-II Vehicle" }

                Text(
                    text = "$vehicleDesc • ${record.fuelType}",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                SeverityChip(severity = severity, isHindi = isHindi)
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextTertiary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun LibraryCategoryChip(
    category: LibraryCategory,
    isHindi: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = NavyCardBackground,
        border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
        modifier = Modifier
            .width(150.dp)
            .clickable { onClick() }
            .testTag("category_chip_${category.id}")
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(NavyCardElevated),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = ElectricBlueLight,
                    modifier = Modifier.size(18.dp)
                )
            }

            Text(
                text = if (isHindi) category.nameHi else category.name,
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "${category.guideCount} Guides",
                color = TextTertiary,
                fontSize = 10.sp
            )
        }
    }
}
