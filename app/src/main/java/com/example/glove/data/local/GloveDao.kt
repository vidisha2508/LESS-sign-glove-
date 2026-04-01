package com.example.glove.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GloveDao {
    @Query("SELECT * FROM layouts")
    fun getAllLayouts(): Flow<List<LayoutEntity>>

    @Query("SELECT * FROM layouts WHERE isSelected = 1 LIMIT 1")
    fun getSelectedLayout(): Flow<LayoutEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLayout(layout: LayoutEntity): Long

    @Update
    suspend fun updateLayout(layout: LayoutEntity)

    @Query("UPDATE layouts SET isSelected = 0 WHERE isSelected = 1")
    suspend fun clearSelectedLayout()

    @Query("UPDATE layouts SET isSelected = 1 WHERE id = :layoutId")
    suspend fun setSelectedLayout(layoutId: Int)

    @Query("DELETE FROM layouts WHERE id = :layoutId")
    suspend fun deleteLayout(layoutId: Int)

    // Gestures mappings calls
    @Query("SELECT * FROM mappings WHERE layoutId = :layoutId ORDER BY gestureIndex ASC")
    fun getMappingsForLayout(layoutId: Int): Flow<List<MappingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMappings(mappings: List<MappingEntity>)

    @Update
    suspend fun updateMapping(mapping: MappingEntity)

    @Query("SELECT * FROM mappings WHERE layoutId = :layoutId AND gestureIndex = :gestureIndex LIMIT 1")
    suspend fun getMappingSync(layoutId: Int, gestureIndex: Int): MappingEntity?
}
