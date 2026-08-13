package com.example.ai

import com.example.model.MaterialCategory

object NigerianCurriculumPromptBuilder {

    fun buildSystemPrompt(): String {
        return """
            You are TeacherMate NG, an expert Nigerian Educational Curriculum Specialist, Senior Master Teacher, and Pedagogical Consultant.
            You are fully versed in:
            1. NERDC (Nigerian Educational Research and Development Council) 9-Year Basic Education Curriculum and Senior Secondary Education Curriculum.
            2. WAEC (West African Examinations Council) WASSCE syllabus & Chief Examiner reporting standards.
            3. NECO (National Examinations Council) SSCE & BECE standards.
            4. UBEC (Universal Basic Education Commission) early childhood, primary, and junior secondary methodologies.
            5. JAMB (Joint Admissions and Matriculation Board) UTME subject requirements.
            
            Your outputs MUST strictly use standard Nigerian educational terminologies, structure, and formatting.
            Always provide comprehensive, detailed, practical, classroom-ready content with zero placeholders.
            Use clean Markdown formatting with clear section headings, bullet points, and numbered lists.
        """.trimIndent()
    }

    fun buildUserPrompt(
        category: MaterialCategory,
        schoolLevel: String,
        subject: String,
        term: String,
        topic: String,
        subTopic: String,
        customInstructions: String
    ): String {
        val baseHeader = """
            EDUCATIONAL CONTEXT:
            - Country: Nigeria 🇳🇬
            - School Level: $schoolLevel
            - Subject: $subject
            - Term: $term
            - Topic: $topic
            ${if (subTopic.isNotBlank()) "- Sub-topic: $subTopic" else ""}
            ${if (customInstructions.isNotBlank()) "- Special Requirements: $customInstructions" else ""}
        """.trimIndent()

        return when (category) {
            MaterialCategory.LESSON_PLAN -> """
                $baseHeader
                
                Generate a comprehensive, standard 8-step Nigerian NERDC Lesson Plan for a 40-minute (or 80-minute double period) classroom session.
                
                Include the following exact Nigerian format sections:
                # LESSON PLAN: $topic
                **Subject:** $subject | **Class:** $schoolLevel | **Term:** $term
                **Topic:** $topic ${if (subTopic.isNotBlank()) "— $subTopic" else ""}
                **Duration:** 40 Minutes | **Average Age:** Appropriate for $schoolLevel
                
                ## 1. BEHAVIORAL OBJECTIVES
                By the end of the lesson, the students/pupils should be able to:
                - (List 3 to 4 specific, measurable cognitive, affective, or psychomotor actions using Bloom's action verbs such as define, identify, explain, demonstrate, calculate, distinguish)
                
                ## 2. INSTRUCTIONAL MATERIALS / TEACHING AIDS
                - Realia / concrete local materials, charts, chalkboard illustrations, flashcards, or apparatus.
                
                ## 3. ENTRY BEHAVIOR / PREVIOUS KNOWLEDGE
                - What the learners already know from prior lessons or daily life in Nigeria.
                
                ## 4. SET INDUCTION / INTRODUCTION (3-5 mins)
                - Interactive hook, thought-provoking question, or short real-world scenario to capture interest.
                
                ## 5. INSTRUCTIONAL PROCEDURES / STEP-BY-STEP PRESENTATION (25 mins)
                - **Step 1: Concept Exploration / Definition** (Teacher's Activity & Students' Activity)
                - **Step 2: Detailed Explanation & Key Principles** (Teacher's Activity & Students' Activity)
                - **Step 3: Worked Examples / Practical Demonstration / Class Discussion**
                - **Step 4: Supervised Practice / Group Activity**
                
                ## 6. EVALUATION / FORMATIVE ASSESSMENT (5 mins)
                - 4 to 5 targeted oral or written questions checking objective achievement.
                
                ## 7. SUMMARY & CONCLUSION (2 mins)
                - Key takeaways and teacher wrap-up note.
                
                ## 8. TAKE-HOME ASSIGNMENT / HOMEWORK
                - 2-3 exercise problems or research tasks for students.
            """.trimIndent()

            MaterialCategory.EXAM_QUESTIONS -> """
                $baseHeader
                
                Generate a complete, standard Nigerian Examination Paper for $schoolLevel $subject ($term).
                Format adhering strictly to WAEC / NECO / BECE examination standards.
                
                Include:
                # $subject EXAMINATION — $term
                **Class:** $schoolLevel | **Time Allowed:** 1 Hour 30 Minutes | **Total Marks:** 100 Marks
                
                ### GENERAL INSTRUCTIONS:
                - Answer ALL questions in Section A and THREE questions in Section B.
                
                ---
                ## SECTION A: OBJECTIVE TEST (MULTIPLE CHOICE)
                Provide 15-20 distinct, high-quality multiple choice questions.
                Each question MUST have options A, B, C, D (or A, B, C for Kindergarten/Primary 1-2).
                
                ---
                ## SECTION B: THEORY / ESSAY QUESTIONS
                Provide 5 structured essay questions (with sub-parts a, b, c) with clearly indicated mark allocations in brackets e.g. [4 marks], [6 marks]. Total marks for theory = 60 marks.
                
                ---
                ## ANSWER KEY & MARKING GUIDE
                Provide the full correct options for Section A (e.g. 1. B, 2. A...) and model solutions/marking breakdown for Section B.
            """.trimIndent()

            MaterialCategory.SCHEME_OF_WORK -> """
                $baseHeader
                
                Generate a complete 12-Week Nigerian NERDC Scheme of Work for $schoolLevel $subject ($term).
                
                Format as a structured table/breakdown for each week (Week 1 through Week 12):
                - **Week 1:** Topic, Sub-topics, Behavioral Objectives, Teacher's Activities, Students' Activities, Teaching Resources.
                - **Week 2 to Week 5:** Sequential progressive curriculum topics.
                - **Week 6:** Mid-Term Break & Continuous Assessment (C.A.) Test.
                - **Week 7 to Week 10:** Advanced curriculum topics.
                - **Week 11:** Revision & General Problem Solving.
                - **Week 12:** End of Term Examination & Compilation of Results.
                
                Ensure full compliance with NERDC National Syllabus sequencing.
            """.trimIndent()

            MaterialCategory.LEARNING_OBJECTIVES -> """
                $baseHeader
                
                Generate comprehensive, standard SMART Behavioral Learning Objectives for $topic in $schoolLevel $subject.
                
                Categorize using Bloom's Revised Taxonomy across the Three Educational Domains:
                ## 1. COGNITIVE DOMAIN (Knowledge, Comprehension, Application, Analysis, Synthesis, Evaluation)
                - State 4-6 specific actionable objectives with active verbs (e.g., Define, Classify, Solve, Differentiate).
                
                ## 2. PSYCHOMOTOR DOMAIN (Practical skills, Hands-on manipulation, Drawing, Measuring, Laboratory dexterity)
                - State 2-4 hands-on motor/practical objectives.
                
                ## 3. AFFECTIVE DOMAIN (Attitudes, Values, Scientific curiosity, National ethics, Environmental consciousness)
                - State 2-3 behavioral attitude outcomes.
                
                ## 4. SUCCESS CRITERIA & PERFORMANCE INDICATORS
                - Clear rubrics for determining when a student has mastered each objective.
            """.trimIndent()

            MaterialCategory.MARKING_SCHEME -> """
                $baseHeader
                
                Generate a comprehensive, official Nigerian Marking Scheme & Grading Guide for $topic in $schoolLevel $subject.
                
                Include:
                ## 1. ASSESSMENT OVERVIEW & SCORE DISTRIBUTION
                - Breakdown of marks for Knowledge, Method, Accuracy, and Final answer (B1, M1, A1, C1 style where applicable).
                
                ## 2. DETAILED STEP-BY-STEP MARKING RUBRIC
                - Question by question step breakdown.
                - Show exact points where half-marks or full-marks are awarded.
                
                ## 3. MODEL ANSWERS & COMMON STUDENT MISCONCEPTIONS
                - Ideal answers expected from top students.
                - Common traps, erroneous answers, and how teachers should penalize minor vs major errors.
                
                ## 4. GRADING BENCHMARK (Nigerian Secondary School Scale: A1, B2, B3, C4, C5, C6, D7, E8, F9).
            """.trimIndent()

            MaterialCategory.RESULT_GENERATOR -> """
                $baseHeader
                
                Generate professional, personalized Nigerian End-of-Term Report Sheet Comments and Performance Assessments.
                
                Include 5 variations based on student performance tiers:
                1. **EXCELLENT / DISTINCTION TIER (80% - 100% | A1):**
                   - Subject Teacher's Remark:
                   - Form Teacher / Class Teacher's Remark:
                   - Principal / Headteacher's Endorsement:
                   - Affective & Psychomotor Traits: Punctuality, Leadership, Neatness, Politeness (Graded A).
                
                2. **CREDIT / ABOVE AVERAGE TIER (65% - 79% | B2 - B3):**
                   - Detailed encouraging comments highlighting strengths and areas for distinction.
                
                3. **AVERAGE / PASS TIER (50% - 64% | C4 - C6):**
                   - Constructive feedback on study habits, homework submission, and focus.
                
                4. **STRUGGLING / NEEDS IMPROVEMENT TIER (Below 50% | D7 - F9):**
                   - Compassionate, motivating corrective advice, parental follow-up recommendations.
                
                5. **SPECIAL TRAITS & CHARACTER REMARKS:**
                   - Extracurricular, discipline, and moral conduct remarks suitable for Nigerian schools.
            """.trimIndent()

            MaterialCategory.QUIZ_GENERATOR -> """
                $baseHeader
                
                Generate an engaging, interactive 10-Question Classroom Pop Quiz for $schoolLevel on $topic ($subject).
                
                Include:
                # CLASSROOM POP QUIZ: $topic
                **Subject:** $subject | **Class:** $schoolLevel
                
                ## QUESTIONS (1 to 10)
                - 7 Multiple Choice Questions (A, B, C, D)
                - 3 Fill-in-the-blank or Short-answer Conceptual Questions
                
                ---
                ## ANSWER KEY & PEDAGOGICAL EXPLANATIONS
                - Correct Answer for each question with a 1-sentence teacher explanation why it is correct.
                - 1 Bonus Challenge Question for gifted/fast learners.
            """.trimIndent()

            MaterialCategory.NOTES_GENERATOR -> """
                $baseHeader
                
                Generate comprehensive, structured Lesson Notes ready for the chalkboard or student distribution for $schoolLevel $subject on $topic.
                
                Include:
                # LESSON NOTE: $topic
                **Subject:** $subject | **Class:** $schoolLevel | **Term:** $term
                
                ## 1. INTRODUCTION & DEFINITION OF TERMS
                - Clear, simple definitions with Nigerian contextual examples.
                
                ## 2. MAIN BODY / DETAILED CONCEPTS & BREAKDOWN
                - Core rules, properties, formulas, historical dates, or scientific facts.
                - Diagrams descriptions or tables where applicable.
                
                ## 3. WORKED EXAMPLES / CASE STUDIES / PRACTICAL APPLICATIONS
                - 2-3 step-by-step fully worked examples.
                
                ## 4. KEY SUMMARY POINTS / REMEMBER THIS
                - 4-5 bullet points summarizing core concepts.
                
                ## 5. CLASSWORK & TAKEAWAY EXERCISES
                - 4 quick classwork questions for immediate student notebook writing.
            """.trimIndent()

            MaterialCategory.CURRICULUM_GUIDE -> """
                $baseHeader
                
                Provide an in-depth Nigerian NERDC Curriculum Guide for $subject in $schoolLevel.
                
                Include:
                # NERDC CURRICULUM GUIDE: $subject ($schoolLevel)
                ## 1. NATIONAL CURRICULUM GOALS & THEMES
                - Core philosophical and developmental objectives set by NERDC/UBEC.
                
                ## 2. TERM-BY-TERM SYLLABUS BREAKDOWN
                - First Term, Second Term, and Third Term key topics.
                
                ## 3. RECOMMENDED TEXTBOOKS & TEACHING RESOURCES
                - Standard Nigerian approved textbook authors (e.g. STAN, MAN, Evans, Learn Africa, Extension, Melrose).
                
                ## 4. WAEC/NECO/BECE KEY EXAM TOPICS & WEIGHTAGE
                - High-yield topics most frequently tested.
            """.trimIndent()
        }
    }
}
