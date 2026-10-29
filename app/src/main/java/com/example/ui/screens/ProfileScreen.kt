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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TechnicianProfile
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

@Composable
fun ProfileScreen(
    profile: TechnicianProfile,
    strings: AppStrings,
    currentLanguage: AppLanguage,
    onToggleLanguage: () -> Unit,
    onUpdateProfile: (name: String, garage: String, level: String, email: String, phone: String) -> Unit,
    onOpenInstallation: () -> Unit,
    onOpenAbout: () -> Unit,
    onLogout: () -> Unit,
    onClearAllData: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditing by remember { mutableStateOf(false) }
    var nameInput by remember { mutableStateOf(profile.name) }
    var garageInput by remember { mutableStateOf(profile.garageName) }
    var emailInput by remember { mutableStateOf(profile.email) }
    var phoneInput by remember { mutableStateOf(profile.phone) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Account & Clear All Data", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = { Text("This will permanently delete your technician profile, saved diagnostic history, repair notes, and bookmarks.", color = TextSecondary) },
            confirmButton = {
                Button(
                    onClick = {
                        onClearAllData()
                        showDeleteConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DiagnosticRed)
                ) {
                    Text(strings.delete, color = TextPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
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
        // Technician Profile Header Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                modifier = Modifier.fillMaxWidth().testTag("profile_main_card")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(NavyCardElevated)
                            .border(2.dp, ElectricBluePrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = ElectricBlueLight,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Text(
                        text = profile.name,
                        color = TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Surface(
                        color = ElectricBluePrimary.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = profile.garageName,
                            color = ElectricBlueLight,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = profile.email,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    OutlinedButton(
                        onClick = { isEditing = !isEditing },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ElectricBlueLight),
                        modifier = Modifier.testTag("edit_profile_toggle_button")
                    ) {
                        Text(if (isEditing) "Close Editing" else "Edit Workshop Details", fontSize = 12.sp)
                    }
                }
            }
        }

        // Editable Form
        if (isEditing) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = NavyCardElevated),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ElectricBluePrimary)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Edit Technician Profile", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)

                        OutlinedTextField(
                            value = nameInput,
                            onValueChange = { nameInput = it },
                            label = { Text(strings.technicianName) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("edit_profile_name"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = ElectricBlueLight,
                                unfocusedBorderColor = NavyBorder
                            )
                        )

                        OutlinedTextField(
                            value = garageInput,
                            onValueChange = { garageInput = it },
                            label = { Text(strings.garageName) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("edit_profile_garage"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = ElectricBlueLight,
                                unfocusedBorderColor = NavyBorder
                            )
                        )

                        Button(
                            onClick = {
                                onUpdateProfile(nameInput, garageInput, profile.experienceLevel, emailInput, phoneInput)
                                isEditing = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricBluePrimary),
                            modifier = Modifier.fillMaxWidth().testTag("save_profile_button")
                        ) {
                            Text("Save Profile Changes", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Settings & Actions Section
        item {
            Text("Settings & Tools", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }

        // Language Option
        item {
            SettingRow(
                icon = Icons.Default.Language,
                title = strings.language,
                subtitle = if (currentLanguage == AppLanguage.HINDI) "हिंदी (Hindi)" else "English",
                onClick = onToggleLanguage
            )
        }

        // Android Installation & APK
        item {
            SettingRow(
                icon = Icons.Default.PhoneAndroid,
                title = strings.installApp,
                subtitle = "PWA Install & Android APK Generation Guide",
                onClick = onOpenInstallation
            )
        }

        // About & Safety Information
        item {
            SettingRow(
                icon = Icons.Default.Shield,
                title = strings.aboutSafety,
                subtitle = "Automotive engineering standards & EV safety rules",
                onClick = onOpenAbout
            )
        }

        // Logout
        item {
            SettingRow(
                icon = Icons.Default.Logout,
                title = "Switch Profile / Sign Out",
                subtitle = "Return to login screen",
                onClick = onLogout
            )
        }

        // Delete Account
        item {
            SettingRow(
                icon = Icons.Default.Delete,
                title = "Delete Account & Clear History",
                subtitle = "Permanent data cleanup",
                titleColor = DiagnosticRed,
                onClick = { showDeleteConfirm = true }
            )
        }
    }
}

@Composable
private fun SettingRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    titleColor: androidx.compose.ui.graphics.Color = TextPrimary,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(NavyCardElevated),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = ElectricBlueLight, modifier = Modifier.size(20.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = titleColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(text = subtitle, color = TextSecondary, fontSize = 11.sp)
            }

            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextTertiary, modifier = Modifier.size(18.dp))
        }
    }
}
