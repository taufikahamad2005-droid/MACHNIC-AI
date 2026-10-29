package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.AppStrings
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
fun InstallationScreen(
    strings: AppStrings,
    currentLanguage: AppLanguage,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDarkBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
    ) {
        // Header
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyCardElevated),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, ElectricBluePrimary),
                modifier = Modifier.fillMaxWidth().testTag("installation_header_card")
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(ElectricBluePrimary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = ElectricBlueLight, modifier = Modifier.size(22.dp))
                        }
                        Text(
                            text = strings.installApp,
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "Mechanic AI is designed to run seamlessly directly on Android phones. Below are your two documented installation pathways.",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // PATH A: Installable PWA
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(
                        color = ElectricBluePrimary.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "PATH A: Instant PWA (No Computer Needed)",
                            color = ElectricBlueLight,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = "Add Mechanic AI to your Android Phone Home Screen via Chrome:",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    val pwaSteps = listOf(
                        "1. Open the shared application URL in Google Chrome on your Android phone.",
                        "2. Tap the Chrome three-dot menu icon (⋮) in the top-right corner.",
                        "3. Select 'Install app' or 'Add to Home screen'.",
                        "4. Confirm the prompt: 'Install Mechanic AI'.",
                        "5. The Mechanic AI icon with custom dark navy & electric blue styling will appear on your phone's app drawer.",
                        "6. It opens in standalone fullscreen mode without the browser URL bar, functioning exactly like a native app."
                    )

                    pwaSteps.forEach { step ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DiagnosticGreen, modifier = Modifier.size(16.dp))
                            Text(step, color = TextSecondary, fontSize = 12.sp, lineHeight = 16.sp)
                        }
                    }
                }
            }
        }

        // PATH B: Native Android APK Build & Install
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(
                        color = DiagnosticGreen.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "PATH B: Native Android APK Export",
                            color = DiagnosticGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = "Export and Install Direct Android APK:",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    val apkSteps = listOf(
                        "1. In Google AI Studio, tap the Settings / Export icon in the top header.",
                        "2. Select 'Download Project as ZIP' or 'Build APK / AAB'.",
                        "3. To compile locally: Open the extracted project in Android Studio or run 'gradle assembleRelease' or 'gradle assembleDebug'.",
                        "4. Copy the compiled app-debug.apk to your phone (via USB, Google Drive, WhatsApp, or Bluetooth).",
                        "5. On your phone: Open Files app, tap app-debug.apk, and enable 'Install from unknown sources' if prompted.",
                        "6. Tap 'Install'. The native app with package identifier com.aistudio.mechanicai.kqzvrt will be installed on your device."
                    )

                    apkSteps.forEach { step ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(Icons.Default.Android, contentDescription = null, tint = ElectricBlueLight, modifier = Modifier.size(16.dp))
                            Text(step, color = TextSecondary, fontSize = 12.sp, lineHeight = 16.sp)
                        }
                    }
                }
            }
        }

        // Permissions and Offline Architecture
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Android System Permissions", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(
                        "• INTERNET: Required to communicate with Gemini AI API for live cloud diagnosis.\n" +
                        "• ACCESS_NETWORK_STATE: Automatically monitors network to switch between Cloud Gemini AI and the Verified Offline Automotive Engine.\n" +
                        "• ROOM SQLite Database: Pure local on-device storage. No personal diagnostic records ever leave your phone without your consent.",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}
