package com.example.glove.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "mappings",
    foreignKeys = [
        ForeignKey(
            entity = LayoutEntity::class,
            parentColumns = ["id"],
            childColumns = ["layoutId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("layoutId")]
)
data class MappingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val layoutId: Int,
    val gestureIndex: Int, // 0 to 31 inside UI
    val mappedWord: String
)
