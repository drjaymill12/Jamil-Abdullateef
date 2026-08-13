package com.example.data

import kotlinx.coroutines.flow.Flow

class MaterialRepository(private val materialDao: MaterialDao) {
    val allMaterials: Flow<List<SavedMaterial>> = materialDao.getAllMaterials()
    val starredMaterials: Flow<List<SavedMaterial>> = materialDao.getStarredMaterials()

    fun getMaterialsByCategory(category: String): Flow<List<SavedMaterial>> {
        return materialDao.getMaterialsByCategory(category)
    }

    fun searchMaterials(query: String): Flow<List<SavedMaterial>> {
        return materialDao.searchMaterials(query)
    }

    suspend fun getMaterialById(id: Long): SavedMaterial? {
        return materialDao.getMaterialById(id)
    }

    suspend fun saveMaterial(material: SavedMaterial): Long {
        return materialDao.insertMaterial(material)
    }

    suspend fun updateMaterial(material: SavedMaterial) {
        materialDao.updateMaterial(material)
    }

    suspend fun deleteMaterial(material: SavedMaterial) {
        materialDao.deleteMaterial(material)
    }

    suspend fun deleteMaterialById(id: Long) {
        materialDao.deleteMaterialById(id)
    }

    suspend fun toggleStarred(id: Long, currentStarred: Boolean) {
        materialDao.setStarred(id, !currentStarred)
    }
}
