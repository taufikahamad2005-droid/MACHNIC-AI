package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diagnostic_records")
data class DiagnosticRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val faultCode: String,
    val vehicleMake: String,
    val vehicleModel: String,
    val modelYear: String,
    val fuelType: String,
    val engineDetails: String,
    val userSymptoms: String,
    val liveSensorData: String,
    val diagnosticTitle: String,
    val diagnosticSummary: String,
    val severityString: String,
    val systemInvolved: String,
    val fullResultJson: String,
    val timestamp: Long = System.currentTimeMillis(),
    val userRepairNotes: String = "",
    val isBookmarked: Boolean = false
)
