package com.example.glove.data.repository

import com.example.glove.data.local.GloveDao
import com.example.glove.data.local.LayoutEntity
import com.example.glove.data.local.MappingEntity
import kotlinx.coroutines.flow.Flow

class GloveRepository(private val dao: GloveDao) {

    val allLayouts: Flow<List<LayoutEntity>> = dao.getAllLayouts()
    val selectedLayout: Flow<LayoutEntity?> = dao.getSelectedLayout()

    fun getMappingsForLayout(layoutId: Int): Flow<List<MappingEntity>> {
        return dao.getMappingsForLayout(layoutId)
    }

    suspend fun createNewLayout(name: String, isSelected: Boolean = false): Int {
        if (isSelected) dao.clearSelectedLayout()
        val newLayoutId = dao.insertLayout(LayoutEntity(name = name, isSelected = isSelected)).toInt()
        
        // Populate 32 options by default
        val defaultMappings = (0..31).map { i ->
            MappingEntity(
                layoutId = newLayoutId,
                gestureIndex = i,
                mappedWord = "Mapped word $i"
            )
        }
        dao.insertMappings(defaultMappings)
        return newLayoutId
    }

    suspend fun selectLayout(layoutId: Int) {
        dao.clearSelectedLayout()
        dao.setSelectedLayout(layoutId)
    }

    suspend fun deleteLayout(layoutId: Int) {
        dao.deleteLayout(layoutId)
    }

    suspend fun updateMappingWord(mapping: MappingEntity, newWord: String) {
        dao.updateMapping(mapping.copy(mappedWord = newWord))
    }

    suspend fun getWordForGesture(layoutId: Int, gestureIndex: Int): String {
        val mapping = dao.getMappingSync(layoutId, gestureIndex)
        return mapping?.mappedWord ?: ""
    }
}
