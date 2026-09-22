package com.example

import com.example.ai.CurriculumTemplateEngine
import com.example.ai.NigerianCurriculumPromptBuilder
import com.example.model.EducationLevel
import com.example.model.MaterialCategory
import com.example.model.NigerianCurriculumData
import com.example.ui.screens.parseLessonPlanSections
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LessonPlanGeneratorTest {

    @Test
    fun testNigerianCurriculumPromptBuilder_createsStructuredLessonPlanPrompt() {
        val prompt = NigerianCurriculumPromptBuilder.buildUserPrompt(
            category = MaterialCategory.LESSON_PLAN,
            schoolLevel = EducationLevel.JSS_2.displayName,
            subject = "Mathematics",
            term = "1st Term",
            topic = "Linear Equations",
            subTopic = "Solving by Factorization",
            customInstructions = "Include WAEC past question pattern"
        )

        assertTrue(prompt.contains("NERDC Lesson Plan"))
        assertTrue(prompt.contains("BEHAVIORAL OBJECTIVES"))
        assertTrue(prompt.contains("INSTRUCTIONAL MATERIALS"))
        assertTrue(prompt.contains("ENTRY BEHAVIOR"))
        assertTrue(prompt.contains("SET INDUCTION"))
        assertTrue(prompt.contains("INSTRUCTIONAL PROCEDURES"))
        assertTrue(prompt.contains("EVALUATION"))
        assertTrue(prompt.contains("TAKE-HOME ASSIGNMENT"))
        assertTrue(prompt.contains("Linear Equations"))
    }

    @Test
    fun testCurriculumTemplateEngine_generatesValid8StepLessonPlan() {
        val generated = CurriculumTemplateEngine.generateOfflineContent(
            category = MaterialCategory.LESSON_PLAN,
            schoolLevel = EducationLevel.SS_1.displayName,
            subject = "Physics",
            term = "1st Term",
            topic = "Newton's Laws of Motion"
        )

        assertNotNull(generated)
        assertTrue(generated.contains("LESSON PLAN"))
        assertTrue(generated.contains("BEHAVIORAL OBJECTIVES"))
        assertTrue(generated.contains("INSTRUCTIONAL PROCEDURES"))

        // Verify sections parser
        val sections = parseLessonPlanSections(generated)
        assertFalse(sections.isEmpty())
        assertTrue(sections.any { it.title.contains("OBJECTIVES", ignoreCase = true) })
    }

    @Test
    fun testNigerianCurriculumData_hasSubjectsAndTopics() {
        val subjects = NigerianCurriculumData.SUBJECTS_BY_LEVEL["Junior Secondary"]
        assertNotNull(subjects)
        assertTrue(subjects!!.contains("Mathematics"))
        assertTrue(subjects.contains("English Language & Literature"))

        val topics = NigerianCurriculumData.TOPIC_SUGGESTIONS["Mathematics"]
        assertNotNull(topics)
        assertTrue(topics!!.isNotEmpty())
    }
}
