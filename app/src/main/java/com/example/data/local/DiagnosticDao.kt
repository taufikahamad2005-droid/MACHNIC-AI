package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DiagnosticDao {

    @Query("SELECT * FROM diagnostic_records ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<DiagnosticRecord>>

    @Query("SELECT * FROM diagnostic_records WHERE isBookmarked = 1 ORDER BY timestamp DESC")
    fun getBookmarkedRecords(): Flow<List<DiagnosticRecord>>

    @Query("SELECT * FROM diagnostic_records WHERE id = :id LIMIT 1")
    suspend fun getRecordById(id: Long): DiagnosticRecord?

    @Query("SELECT * FROM diagnostic_records WHERE faultCode LIKE '%' || :query || '%' OR vehicleMake LIKE '%' || :query || '%' OR vehicleModel LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchRecords(query: String): Flow<List<DiagnosticRecord>>

    @Query("SELECT * FROM diagnostic_records ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentRecords(limit: Int): Flow<List<DiagnosticRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: DiagnosticRecord): Long

    @Query("UPDATE diagnostic_records SET userRepairNotes = :notes WHERE id = :id")
    suspend fun updateRepairNotes(id: Long, notes: String)

    @Query("UPDATE diagnostic_records SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun setBookmark(id: Long, isBookmarked: Boolean)

    @Query("DELETE FROM diagnostic_records WHERE id = :id")
    suspend fun deleteRecordById(id: Long)

    @Query("DELETE FROM diagnostic_records")
    suspend fun clearAllRecords()
}
