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
    LESSON_PLAN,
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

    // --- Dedicated Lesson Plan Generator State ---
    private val _lessonPlanSubject = MutableStateFlow("Mathematics")
    val lessonPlanSubject: StateFlow<String> = _lessonPlanSubject.asStateFlow()

    private val _lessonPlanGrade = MutableStateFlow(EducationLevel.JSS_2)
    val lessonPlanGrade: StateFlow<EducationLevel> = _lessonPlanGrade.asStateFlow()

    private val _lessonPlanTopic = MutableStateFlow("Linear Equations and Graphical Solution")
    val lessonPlanTopic: StateFlow<String> = _lessonPlanTopic.asStateFlow()

    private val _lessonPlanSubTopic = MutableStateFlow("Solving Linear Equations with Two Unknowns")
    val lessonPlanSubTopic: StateFlow<String> = _lessonPlanSubTopic.asStateFlow()

    private val _lessonPlanTerm = MutableStateFlow(SchoolTerm.FIRST_TERM)
    val lessonPlanTerm: StateFlow<SchoolTerm> = _lessonPlanTerm.asStateFlow()

    private val _lessonPlanDuration = MutableStateFlow("40 Minutes (Single Period)")
    val lessonPlanDuration: StateFlow<String> = _lessonPlanDuration.asStateFlow()

    private val _lessonPlanAids = MutableStateFlow("Graph sheets, ruler, chalkboard grid, flashcards")
    val lessonPlanAids: StateFlow<String> = _lessonPlanAids.asStateFlow()

    private val _lessonPlanCustomNotes = MutableStateFlow("")
    val lessonPlanCustomNotes: StateFlow<String> = _lessonPlanCustomNotes.asStateFlow()

    private val _activeLessonPlan = MutableStateFlow<SavedMaterial?>(null)
    val activeLessonPlan: StateFlow<SavedMaterial?> = _activeLessonPlan.asStateFlow()

    private val _isGeneratingPlan = MutableStateFlow(false)
    val isGeneratingPlan: StateFlow<Boolean> = _isGeneratingPlan.asStateFlow()

    private val _lessonPlanProgressStep = MutableStateFlow("")
    val lessonPlanProgressStep: StateFlow<String> = _lessonPlanProgressStep.asStateFlow()

    private val _lessonPlanAiNotice = MutableStateFlow<String?>(null)
    val lessonPlanAiNotice: StateFlow<String?> = _lessonPlanAiNotice.asStateFlow()

    private val _lessonPlanError = MutableStateFlow<String?>(null)
    val lessonPlanError: StateFlow<String?> = _lessonPlanError.asStateFlow()

    fun setLessonPlanSubject(subject: String) {
        _lessonPlanSubject.value = subject
    }

    fun setLessonPlanGrade(grade: EducationLevel) {
        _lessonPlanGrade.value = grade
    }

    fun setLessonPlanTopic(topic: String) {
        _lessonPlanTopic.value = topic
    }

    fun setLessonPlanSubTopic(subTopic: String) {
        _lessonPlanSubTopic.value = subTopic
    }

    fun setLessonPlanTerm(term: SchoolTerm) {
        _lessonPlanTerm.value = term
    }

    fun setLessonPlanDuration(duration: String) {
        _lessonPlanDuration.value = duration
    }

    fun setLessonPlanAids(aids: String) {
        _lessonPlanAids.value = aids
    }

    fun setLessonPlanCustomNotes(notes: String) {
        _lessonPlanCustomNotes.value = notes
    }

    fun clearLessonPlanInputs() {
        _lessonPlanTopic.value = ""
        _lessonPlanSubTopic.value = ""
        _lessonPlanCustomNotes.value = ""
        _activeLessonPlan.value = null
        _lessonPlanError.value = null
        _lessonPlanAiNotice.value = null
    }

    fun prefillAndNavigateToLessonPlan(subject: String, grade: EducationLevel, topic: String) {
        _lessonPlanSubject.value = subject
        _lessonPlanGrade.value = grade
        _lessonPlanTopic.value = topic
        _lessonPlanSubTopic.value = ""
        _activeLessonPlan.value = null
        _currentNavTab.value = AppNavTab.LESSON_PLAN
    }

    fun generateLessonPlanFromScreen() {
        val topic = _lessonPlanTopic.value.trim()
        if (topic.isBlank()) {
            _lessonPlanError.value = "Please enter a lesson topic"
            return
        }

        viewModelScope.launch {
            _isGeneratingPlan.value = true
            _lessonPlanError.value = null
            _lessonPlanProgressStep.value = "Connecting to Gemini 3.5 API..."

            val grade = _lessonPlanGrade.value
            val subject = _lessonPlanSubject.value
            val term = _lessonPlanTerm.value
            val subTopic = _lessonPlanSubTopic.value
            val duration = _lessonPlanDuration.value
            val aids = _lessonPlanAids.value
            val customNotes = _lessonPlanCustomNotes.value

            val combinedInstructions = buildString {
                append("Duration: $duration. ")
                if (aids.isNotBlank()) append("Preferred Instructional Aids: $aids. ")
                if (customNotes.isNotBlank()) append("Special Pedagogical Focus: $customNotes.")
            }

            try {
                _lessonPlanProgressStep.value = "Structuring 8-step NERDC presentation..."
                val result = GeminiAiService.generateMaterial(
                    category = MaterialCategory.LESSON_PLAN,
                    schoolLevel = grade.displayName,
                    subject = subject,
                    term = term.displayName,
                    topic = topic,
                    subTopic = subTopic,
                    customInstructions = combinedInstructions
                )

                val newMaterial = SavedMaterial(
                    category = MaterialCategory.LESSON_PLAN.name,
                    title = "Lesson Plan: $topic",
                    educationLevel = grade.displayName,
                    subject = subject,
                    term = term.displayName,
                    topic = topic,
                    subTopic = subTopic,
                    content = result.content,
                    createdAt = System.currentTimeMillis()
                )

                val savedId = repository.saveMaterial(newMaterial)
                val savedItem = newMaterial.copy(id = savedId)

                _activeLessonPlan.value = savedItem
                _lessonPlanAiNotice.value = result.notice ?: (if (result.isAiGenerated) "Generated with Gemini 3.5 AI" else "Generated with Offline NERDC Engine")
                _isGeneratingPlan.value = false
            } catch (e: Exception) {
                _isGeneratingPlan.value = false
                _lessonPlanError.value = "Failed to generate: ${e.message}"
            }
        }
    }

    fun refineActiveLessonPlan(instruction: String) {
        val current = _activeLessonPlan.value ?: return
        viewModelScope.launch {
            _isGeneratingPlan.value = true
            _lessonPlanProgressStep.value = "Refining lesson plan with Gemini AI..."
            try {
                val result = GeminiAiService.refineLessonPlan(current.content, instruction)
                val updated = current.copy(content = result.content)
                repository.updateMaterial(updated)
                _activeLessonPlan.value = updated
                _lessonPlanAiNotice.value = result.notice
                _isGeneratingPlan.value = false
            } catch (e: Exception) {
                _isGeneratingPlan.value = false
                _lessonPlanError.value = "Refinement failed: ${e.message}"
            }
        }
    }

    fun updateActivePlanContent(newContent: String) {
        val current = _activeLessonPlan.value ?: return
        viewModelScope.launch {
            val updated = current.copy(content = newContent)
            repository.updateMaterial(updated)
            _activeLessonPlan.value = updated
        }
    }

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
