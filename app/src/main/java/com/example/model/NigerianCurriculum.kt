package com.example.model

enum class EducationLevel(val displayName: String, val category: String, val ageGroup: String) {
    // Early Years / Kindergarten
    KG_1("Kindergarten 1 (KG 1)", "Kindergarten", "Ages 3-4"),
    KG_2("Kindergarten 2 (KG 2)", "Kindergarten", "Ages 4-5"),
    NURSERY_1("Nursery 1", "Kindergarten", "Ages 3-4"),
    NURSERY_2("Nursery 2", "Kindergarten", "Ages 4-5"),
    
    // Primary / Basic 1-6
    PRIMARY_1("Primary 1 (Basic 1)", "Primary", "Ages 5-6"),
    PRIMARY_2("Primary 2 (Basic 2)", "Primary", "Ages 6-7"),
    PRIMARY_3("Primary 3 (Basic 3)", "Primary", "Ages 7-8"),
    PRIMARY_4("Primary 4 (Basic 4)", "Primary", "Ages 8-9"),
    PRIMARY_5("Primary 5 (Basic 5)", "Primary", "Ages 9-10"),
    PRIMARY_6("Primary 6 (Basic 6)", "Primary", "Ages 10-11"),
    
    // Junior Secondary / Basic 7-9 (BECE)
    JSS_1("JSS 1 (Basic 7)", "Junior Secondary", "Ages 11-12"),
    JSS_2("JSS 2 (Basic 8)", "Junior Secondary", "Ages 12-13"),
    JSS_3("JSS 3 (Basic 9 - BECE)", "Junior Secondary", "Ages 13-14"),
    
    // Senior Secondary (WAEC / NECO / JAMB)
    SS_1("SS 1 (Senior Secondary 1)", "Senior Secondary", "Ages 14-15"),
    SS_2("SS 2 (Senior Secondary 2)", "Senior Secondary", "Ages 15-16"),
    SS_3("SS 3 (Senior Secondary 3 - WAEC/NECO)", "Senior Secondary", "Ages 16-18")
}

enum class SchoolTerm(val displayName: String, val weeks: String) {
    FIRST_TERM("1st Term (First Term)", "Weeks 1 - 13 (Sept - Dec)"),
    SECOND_TERM("2nd Term (Second Term)", "Weeks 1 - 12 (Jan - Apr)"),
    THIRD_TERM("3rd Term (Third Term)", "Weeks 1 - 12 (Apr - Jul)")
}

enum class MaterialCategory(
    val title: String,
    val iconEmoji: String,
    val description: String,
    val samplePromptPlaceholder: String
) {
    LESSON_PLAN(
        title = "Lesson Plan",
        iconEmoji = "📚",
        description = "Standard 8-step Nigerian NERDC Lesson Plan with behavioral objectives, instructional materials, step-by-step presentation, evaluation & homework.",
        samplePromptPlaceholder = "e.g., Photosynthesis, Quadratic Equations, Parts of Speech, Civic Rights"
    ),
    EXAM_QUESTIONS(
        title = "Exam Questions",
        iconEmoji = "📝",
        description = "Termly Examination or CA Tests with Section A (Objectives A-D) & Section B (Theory/Essay) aligned with WAEC/NECO/BECE format.",
        samplePromptPlaceholder = "e.g., First Term Examination covering Weeks 1 to 10"
    ),
    SCHEME_OF_WORK(
        title = "Scheme of Work",
        iconEmoji = "📖",
        description = "Complete 12-week NERDC termly breakdown with weekly topics, subtopics, teacher/learner activities, and teaching aids.",
        samplePromptPlaceholder = "e.g., Entire 1st Term Scheme for JSS 2 Mathematics"
    ),
    LEARNING_OBJECTIVES(
        title = "Learning Objectives",
        iconEmoji = "🎯",
        description = "SMART behavioral objectives mapped across Cognitive, Affective, and Psychomotor domains using Bloom's Taxonomy.",
        samplePromptPlaceholder = "e.g., Objectives for Nigerian Civil War, Chemical Bonding, Fractions"
    ),
    MARKING_SCHEME(
        title = "Marking Scheme",
        iconEmoji = "✅",
        description = "Comprehensive scoring guide, step-by-step mark distribution, model solutions, and common student misconceptions.",
        samplePromptPlaceholder = "e.g., Marking Guide for SS2 Chemistry Mock Exam"
    ),
    RESULT_GENERATOR(
        title = "Result Generator",
        iconEmoji = "📊",
        description = "End-of-term student report card comments, continuous assessment performance remarks, and behavioral trait evaluation.",
        samplePromptPlaceholder = "e.g., 85% aggregate student with strong leadership, or struggling in numeracy"
    ),
    QUIZ_GENERATOR(
        title = "Quiz Generator",
        iconEmoji = "🧠",
        description = "Quick 5-15 question classroom pop quizzes, continuous assessment bell ringers with answer keys and explanations.",
        samplePromptPlaceholder = "e.g., 10-Question Quick Quiz on Nigerian Geography & Capitals"
    ),
    NOTES_GENERATOR(
        title = "Notes Generator",
        iconEmoji = "📄",
        description = "Comprehensive chalkboard and student-ready lesson notes with clear headings, worked examples, illustrations, and classwork.",
        samplePromptPlaceholder = "e.g., Detailed Lesson Note on Newton's Laws of Motion"
    ),
    CURRICULUM_GUIDE(
        title = "Nigerian Curriculum",
        iconEmoji = "🇳🇬",
        description = "Explore NERDC National Curriculum syllabi, core themes, key performance indicators, and recommended textbooks.",
        samplePromptPlaceholder = "e.g., View NERDC Syllabus for SS1 Physics"
    )
}

object NigerianCurriculumData {
    val SUBJECTS_BY_LEVEL = mapOf(
        "Kindergarten" to listOf(
            "Numeracy / Early Mathematics",
            "Literacy / Phonics & Letter Work",
            "Basic Science & Nature Study",
            "Social Habits & Civic Education",
            "Health Habits & Hygiene",
            "Rhymes & Storytelling",
            "Creative & Cultural Arts / Coloring",
            "Handwriting"
        ),
        "Primary" to listOf(
            "Mathematics",
            "English Studies / Grammar & Composition",
            "Basic Science and Technology",
            "Social Studies",
            "Civic Education",
            "Agricultural Science",
            "Information Technology (ICT)",
            "Cultural and Creative Arts (CCA)",
            "Christian Religious Studies (CRS)",
            "Islamic Religious Studies (IRS)",
            "Physical and Health Education (PHE)",
            "Home Economics",
            "Yoruba Language",
            "Igbo Language",
            "Hausa Language",
            "French Language"
        ),
        "Junior Secondary" to listOf(
            "Mathematics",
            "English Language & Literature",
            "Basic Science",
            "Basic Technology",
            "Social Studies",
            "Civic Education",
            "Agricultural Science",
            "Computer Studies / ICT",
            "Business Studies",
            "Cultural and Creative Arts (CCA)",
            "Christian Religious Studies (CRS)",
            "Islamic Religious Studies (IRS)",
            "Physical and Health Education (PHE)",
            "Home Economics",
            "French",
            "Yoruba",
            "Igbo",
            "Hausa"
        ),
        "Senior Secondary" to listOf(
            "Mathematics",
            "English Language",
            "Civic Education",
            "Biology",
            "Chemistry",
            "Physics",
            "Further Mathematics",
            "Agricultural Science",
            "Computer Studies / ICT",
            "Economics",
            "Government",
            "Literature-in-English",
            "Commerce",
            "Financial Accounting",
            "Geography",
            "Christian Religious Studies (CRS)",
            "Islamic Religious Studies (IRS)",
            "Technical Drawing",
            "Food and Nutrition",
            "Yoruba",
            "Igbo",
            "Hausa"
        )
    )

    val TOPIC_SUGGESTIONS = mapOf(
        "Mathematics" to listOf(
            "Fractions, Decimals and Percentages",
            "Algebraic Expressions & Factorization",
            "Linear and Quadratic Equations",
            "Pythagoras Theorem & Trigonometry",
            "Statistics: Mean, Median, Mode & Histograms",
            "Mensuration: Perimeter, Area & Volume",
            "Sets and Venn Diagrams",
            "Logarithms and Indices",
            "Probability & Permutations",
            "Commercial Arithmetic: Simple & Compound Interest"
        ),
        "English Language" to listOf(
            "Parts of Speech: Nouns, Verbs, Prepositions",
            "Tenses and Subject-Verb Agreement (Concord)",
            "Formal and Informal Letter Writing",
            "Narrative and Argumentative Essay Writing",
            "Comprehension Passages & Summary Writing",
            "Figures of Speech: Simile, Metaphor, Irony",
            "Phonetics: Vowel and Consonant Sounds, Stress",
            "Direct and Indirect Speech",
            "Clauses and Sentence Types",
            "Idiomatic Expressions and Phrasal Verbs"
        ),
        "Basic Science" to listOf(
            "Living and Non-Living Things",
            "The Human Body Systems (Digestive & Circulatory)",
            "Energy: Forms, Transformations and Conservation",
            "Matter: States, Atoms, Molecules & Elements",
            "Environmental Hazards: Pollution & Erosion",
            "Magnetism and Electricity Basics",
            "Reproduction in Plants and Animals",
            "Solar System and Earth Movement",
            "Drugs and Substance Abuse Prevention"
        ),
        "Physics" to listOf(
            "Units, Dimensions and Measurements",
            "Vectors and Scalars",
            "Motion: Linear, Projectile and Circular",
            "Newton's Laws of Motion and Friction",
            "Work, Energy and Power",
            "Heat Transfer and Thermal Expansion",
            "Wave Motion, Light Optics & Refraction",
            "Electric Current, Ohm's Law and Circuits",
            "Electromagnetism & Transformers",
            "Atomic and Nuclear Physics Basics"
        ),
        "Chemistry" to listOf(
            "Atomic Structure, Isotopes and Periodic Table",
            "Chemical Bonding: Ionic, Covalent, Metallic",
            "Acids, Bases, Salts and pH Scale",
            "Stoichiometry and Mole Concept",
            "States of Matter and Gas Laws (Boyle's, Charles')",
            "Electrolysis and Faraday's Laws",
            "Hydrocarbons: Alkanes, Alkenes, Alkynes",
            "Rates of Chemical Reactions & Equilibrium",
            "Metals and Non-metals Extraction",
            "Water, Air and Environmental Chemistry"
        ),
        "Biology" to listOf(
            "Cell Structure, Functions and Organization",
            "Classification of Living Organisms",
            "Plant Nutrition: Photosynthesis and Mineral Salts",
            "Animal Nutrition: Enzymes and Digestion",
            "Transport System in Plants and Mammals",
            "Respiration and Gas Exchange",
            "Excretion and Homeostasis",
            "Ecology: Habitats, Food Chains and Ecosystems",
            "Genetics: Heredity, Mendel's Laws and DNA",
            "Evolution and Adaptation Mechanisms"
        ),
        "Civic Education" to listOf(
            "Values, Norms and National Consciousness",
            "Citizenship: Rights, Duties and Obligations",
            "Democracy and Rule of Law in Nigeria",
            "Human Rights and Prevention of Child Labor",
            "Constitution and Arms of Government in Nigeria",
            "Cultism, Drug Abuse and Youth Restiveness",
            "Public Service and Fighting Corruption (EFCC/ICPC)",
            "Electoral Process and Free Elections (INEC)"
        ),
        "Economics" to listOf(
            "Meaning of Economics, Scarcity and Opportunity Cost",
            "Theory of Demand and Supply & Equilibrium",
            "Elasticity of Demand and Supply",
            "Production and Factors of Production",
            "Market Structures: Perfect & Imperfect Markets",
            "Money, Banking and Central Bank of Nigeria (CBN)",
            "National Income Accounting (GDP, GNP)",
            "Inflation, Unemployment and Fiscal Policy",
            "International Trade and Balance of Payments"
        )
    )
}
