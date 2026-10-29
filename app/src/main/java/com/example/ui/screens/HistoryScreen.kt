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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DiagnosticRecord
import com.example.data.model.SeverityLevel
import com.example.ui.components.SeverityChip
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.AppStrings
import com.example.ui.theme.DiagnosticAmber
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    records: List<DiagnosticRecord>,
    bookmarkedRecords: List<DiagnosticRecord>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    strings: AppStrings,
    currentLanguage: AppLanguage,
    onReopenRecord: (DiagnosticRecord) -> Unit,
    onDeleteRecord: (Long) -> Unit,
    onToggleBookmark: (Long, Boolean) -> Unit,
    onClearAllHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showClearDialog by remember { mutableStateOf(false) }
    val isHindi = currentLanguage == AppLanguage.HINDI

    val activeList = if (selectedTab == 0) records else bookmarkedRecords

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text(strings.clearHistory, color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = { Text(strings.clearHistoryConfirm, color = TextSecondary, fontSize = 13.sp) },
            confirmButton = {
                Button(
                    onClick = {
                        onClearAllHistory()
                        showClearDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DiagnosticRed)
                ) {
                    Text(strings.confirm, color = TextPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text(strings.cancel, color = TextSecondary)
                }
            },
            containerColor = NavyCardBackground
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDarkBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
    ) {
        // Tab Selection: All History vs Bookmarked
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = NavyCardBackground,
                contentColor = ElectricBlueLight,
                indicator = {},
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            "${strings.history} (${records.size})",
                            fontSize = 13.sp,
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 0) ElectricBlueLight else TextSecondary
                        )
                    },
                    modifier = Modifier.testTag("history_tab_all")
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            "${strings.savedGuides} (${bookmarkedRecords.size})",
                            fontSize = 13.sp,
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 1) ElectricBlueLight else TextSecondary
                        )
                    },
                    modifier = Modifier.testTag("history_tab_bookmarks")
                )
            }
        }

        // Search & Filter Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text(strings.filterByCode, color = TextTertiary, fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = ElectricBlueLight) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("history_search_input"),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ElectricBluePrimary,
                    unfocusedBorderColor = NavyBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = NavyCardBackground,
                    unfocusedContainerColor = NavyCardBackground
                )
            )
        }

        if (activeList.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (selectedTab == 0) Icons.Default.History else Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            tint = TextTertiary,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (selectedTab == 0) strings.noSearchesYet else strings.noSavedGuides,
                            color = TextSecondary,
                            fontSize = 13.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(activeList, key = { it.id }) { record ->
                HistoryItemCard(
                    record = record,
                    isHindi = isHindi,
                    onClick = { onReopenRecord(record) },
                    onDelete = { onDeleteRecord(record.id) },
                    onToggleBookmark = { onToggleBookmark(record.id, record.isBookmarked) }
                )
            }

            if (selectedTab == 0 && records.isNotEmpty()) {
                item {
                    OutlinedButton(
                        onClick = { showClearDialog = true },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DiagnosticRed),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DiagnosticRed.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth().testTag("clear_history_button")
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(strings.clearHistory, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryItemCard(
    record: DiagnosticRecord,
    isHindi: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onToggleBookmark: () -> Unit
) {
    val severity = try {
        SeverityLevel.valueOf(record.severityString)
    } catch (e: Exception) {
        SeverityLevel.MEDIUM
    }

    val dateFormatted = remember(record.timestamp) {
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        sdf.format(Date(record.timestamp))
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("history_item_${record.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = NavyCardElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, ElectricBluePrimary)
                ) {
                    Text(
                        text = record.faultCode,
                        color = ElectricBlueLight,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = dateFormatted,
                    color = TextTertiary,
                    fontSize = 10.sp
                )

                Row {
                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier.size(32.dp).testTag("history_bookmark_${record.id}")
                    ) {
                        Icon(
                            imageVector = if (record.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (record.isBookmarked) DiagnosticAmber else TextTertiary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp).testTag("history_delete_${record.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = TextTertiary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Text(
                text = record.diagnosticTitle,
                color = TextPrimary,
                fontSize = 14.sp,
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
                fontSize = 12.sp
            )

            if (record.userRepairNotes.isNotBlank()) {
                Surface(
                    color = NavyDarkBackground,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Notes: ${record.userRepairNotes}",
                        color = ElectricBlueLight,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            SeverityChip(severity = severity, isHindi = isHindi)
        }
    }
}
