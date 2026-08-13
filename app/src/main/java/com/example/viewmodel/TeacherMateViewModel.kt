package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiAiService
import com.example.data.AppDatabase
import com.example.data.MaterialRepository
import com.example.data.SavedMaterial
import com.example.model.EducationLevel
import com.example.model.MaterialCategory
import com.example.model.NigerianCurriculumData
import com.example.model.SchoolTerm
import com.example.util.PdfExporter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

enum class AppNavTab {
    HOME,
    LIBRARY,
    CURRICULUM,
    RESULT_TOOL
}

data class GenerationState(
    val isGenerating: Boolean = false,
    val progressMessage: String = "",
    val error: String? = null
)

class TeacherMateViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MaterialRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = MaterialRepository(db.materialDao())
    }

    val savedMaterials: StateFlow<List<SavedMaterial>> = repository.allMaterials
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val starredMaterials: StateFlow<List<SavedMaterial>> = repository.starredMaterials
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentNavTab = MutableStateFlow(AppNavTab.HOME)
    val currentNavTab: StateFlow<AppNavTab> = _currentNavTab.asStateFlow()

    private val _selectedLevelFilter = MutableStateFlow("All")
    val selectedLevelFilter: StateFlow<String> = _selectedLevelFilter.asStateFlow()

    private val _generationState = MutableStateFlow(GenerationState())
    val generationState: StateFlow<GenerationState> = _generationState.asStateFlow()

    private val _currentViewingMaterial = MutableStateFlow<SavedMaterial?>(null)
    val currentViewingMaterial: StateFlow<SavedMaterial?> = _currentViewingMaterial.asStateFlow()

    private val _selectedCategoryForDialog = MutableStateFlow<MaterialCategory?>(null)
    val selectedCategoryForDialog: StateFlow<MaterialCategory?> = _selectedCategoryForDialog.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun setNavTab(tab: AppNavTab) {
        _currentNavTab.value = tab
    }

    fun setLevelFilter(filter: String) {
        _selectedLevelFilter.value = filter
    }

    fun openGeneratorDialog(category: MaterialCategory) {
        _selectedCategoryForDialog.value = category
    }

    fun closeGeneratorDialog() {
        _selectedCategoryForDialog.value = null
    }

    fun viewMaterial(material: SavedMaterial?) {
        _currentViewingMaterial.value = material
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun generateMaterial(
        category: MaterialCategory,
        schoolLevel: EducationLevel,
        subject: String,
        term: SchoolTerm,
        topic: String,
        subTopic: String = "",
        customInstructions: String = "",
        onCompleted: (SavedMaterial) -> Unit
    ) {
        viewModelScope.launch {
            _generationState.value = GenerationState(
                isGenerating = true,
                progressMessage = "Compiling ${category.title} aligned with Nigerian ${schoolLevel.category} NERDC Curriculum..."
            )

            try {
                val result = GeminiAiService.generateMaterial(
                    category = category,
                    schoolLevel = schoolLevel.displayName,
                    subject = subject,
                    term = term.displayName,
                    topic = topic,
                    subTopic = subTopic,
                    customInstructions = customInstructions
                )

                val newMaterial = SavedMaterial(
                    category = category.name,
                    title = "${category.title}: $topic",
                    educationLevel = schoolLevel.displayName,
                    subject = subject,
                    term = term.displayName,
                    topic = topic,
                    subTopic = subTopic,
                    content = result.content,
                    createdAt = System.currentTimeMillis()
                )

                val savedId = repository.saveMaterial(newMaterial)
                val finalMaterial = newMaterial.copy(id = savedId)

                _generationState.value = GenerationState(isGenerating = false)
                _currentViewingMaterial.value = finalMaterial
                closeGeneratorDialog()
                onCompleted(finalMaterial)

            } catch (e: Exception) {
                _generationState.value = GenerationState(
                    isGenerating = false,
                    error = "Failed to generate material: ${e.message}"
                )
            }
        }
    }

    fun updateMaterialContent(material: SavedMaterial, newContent: String) {
        viewModelScope.launch {
            val updated = material.copy(content = newContent)
            repository.updateMaterial(updated)
            _currentViewingMaterial.value = updated
        }
    }

    fun toggleStarred(material: SavedMaterial) {
        viewModelScope.launch {
            repository.toggleStarred(material.id, material.isStarred)
            if (_currentViewingMaterial.value?.id == material.id) {
                _currentViewingMaterial.value = _currentViewingMaterial.value?.copy(isStarred = !material.isStarred)
            }
        }
    }

    fun deleteMaterial(material: SavedMaterial) {
        viewModelScope.launch {
            repository.deleteMaterial(material)
            if (_currentViewingMaterial.value?.id == material.id) {
                _currentViewingMaterial.value = null
            }
        }
    }

    fun exportAndSharePdf(context: Context, material: SavedMaterial) {
        val file = PdfExporter.exportToPdfFile(context, material)
        if (file != null) {
            PdfExporter.sharePdf(context, file, material.title)
        } else {
            Toast.makeText(context, "Failed to create PDF", Toast.LENGTH_SHORT).show()
        }
    }

    fun exportAndOpenPdf(context: Context, material: SavedMaterial) {
        val file = PdfExporter.exportToPdfFile(context, material)
        if (file != null) {
            Toast.makeText(context, "PDF saved to Documents folder", Toast.LENGTH_SHORT).show()
            PdfExporter.openPdf(context, file)
        } else {
            Toast.makeText(context, "Failed to create PDF", Toast.LENGTH_SHORT).show()
        }
    }
}
