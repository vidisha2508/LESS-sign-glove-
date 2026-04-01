package com.example.glove.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LayoutEntity::class, MappingEntity::class], version = 1, exportSchema = false)
abstract class GloveDatabase : RoomDatabase() {
    abstract fun gloveDao(): GloveDao

    companion object {
        @Volatile
        private var INSTANCE: GloveDatabase? = null

        fun getDatabase(context: Context): GloveDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GloveDatabase::class.java,
                    "glove_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
