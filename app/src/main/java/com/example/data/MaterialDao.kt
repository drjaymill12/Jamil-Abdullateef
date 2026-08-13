package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MaterialDao {
    @Query("SELECT * FROM saved_materials ORDER BY createdAt DESC")
    fun getAllMaterials(): Flow<List<SavedMaterial>>

    @Query("SELECT * FROM saved_materials WHERE category = :category ORDER BY createdAt DESC")
    fun getMaterialsByCategory(category: String): Flow<List<SavedMaterial>>

    @Query("SELECT * FROM saved_materials WHERE isStarred = 1 ORDER BY createdAt DESC")
    fun getStarredMaterials(): Flow<List<SavedMaterial>>

    @Query("SELECT * FROM saved_materials WHERE id = :id")
    suspend fun getMaterialById(id: Long): SavedMaterial?

    @Query("SELECT * FROM saved_materials WHERE title LIKE '%' || :query || '%' OR topic LIKE '%' || :query || '%' OR subject LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchMaterials(query: String): Flow<List<SavedMaterial>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaterial(material: SavedMaterial): Long

    @Update
    suspend fun updateMaterial(material: SavedMaterial)

    @Delete
    suspend fun deleteMaterial(material: SavedMaterial)

    @Query("DELETE FROM saved_materials WHERE id = :id")
    suspend fun deleteMaterialById(id: Long)

    @Query("UPDATE saved_materials SET isStarred = :isStarred WHERE id = :id")
    suspend fun setStarred(id: Long, isStarred: Boolean)
}
