package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
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
fun AboutScreen(
    strings: AppStrings,
    currentLanguage: AppLanguage,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDarkBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
    ) {
        // App Identity
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyCardElevated),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, ElectricBluePrimary),
                modifier = Modifier.fillMaxWidth().testTag("about_main_card")
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(NavyDarkBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Build, contentDescription = null, tint = ElectricBlueLight, modifier = Modifier.size(32.dp))
                    }

                    Text(text = strings.appName, color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Version 1.0.0 Production Release", color = ElectricBlueLight, fontSize = 12.sp)
                    Text(text = strings.tagline, color = TextSecondary, fontSize = 13.sp)
                }
            }
        }

        // Safety Disclaimer
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DiagnosticAmber.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = DiagnosticAmber, modifier = Modifier.size(20.dp))
                        Text("Technical & Legal Disclaimer", color = DiagnosticAmber, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = strings.safetyDisclaimer,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            }
        }

        // EV / Hybrid High Voltage Warning
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DiagnosticRed.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.ElectricBolt, contentDescription = null, tint = DiagnosticRed, modifier = Modifier.size(20.dp))
                        Text("Electric & Hybrid Vehicle Caution (400V - 800V)", color = DiagnosticRed, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = strings.highVoltageWarning,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            }
        }

        // Engineering Principles
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Mechanic AI Engineering Principles", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("1. Test Before Replacement: Fault codes represent detected sensor symptoms, not necessarily a defective part.", color = TextSecondary, fontSize = 12.sp)
                    Text("2. Ground Circuit Checks: >70% of automotive electrical faults stem from poor chassis grounds or terminal pin fretting.", color = TextSecondary, fontSize = 12.sp)
                    Text("3. Free & Unrestricted: This application has no subscriptions, ads, or paywalls for professional mechanics.", color = TextSecondary, fontSize = 12.sp)
                }
            }
        }
    }
}
