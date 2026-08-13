package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_materials")
data class SavedMaterial(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String, // LESSON_PLAN, EXAM_QUESTIONS, SCHEME_OF_WORK, etc.
    val title: String,
    val educationLevel: String,
    val subject: String,
    val term: String,
    val topic: String,
    val subTopic: String = "",
    val content: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isStarred: Boolean = false,
    val tags: String = ""
)
