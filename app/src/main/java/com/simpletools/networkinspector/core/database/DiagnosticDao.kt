package com.simpletools.networkinspector.core.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface DiagnosticDao {
    @Query("SELECT * FROM diagnostics ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<DiagnosticEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: DiagnosticEntity)

    @Delete
    suspend fun delete(entity: DiagnosticEntity)

    @Query("DELETE FROM diagnostics")
    suspend fun deleteAll()
}

@Database(entities = [DiagnosticEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun diagnosticDao(): DiagnosticDao
}
