package com.example.ai

import com.example.model.MaterialCategory

object CurriculumTemplateEngine {

    fun generateOfflineContent(
        category: MaterialCategory,
        schoolLevel: String,
        subject: String,
        term: String,
        topic: String,
        subTopic: String = "",
        customNote: String = ""
    ): String {
        val topicDisplay = if (subTopic.isNotBlank()) "$topic: $subTopic" else topic

        return when (category) {
            MaterialCategory.LESSON_PLAN -> generateLessonPlan(schoolLevel, subject, term, topicDisplay)
            MaterialCategory.EXAM_QUESTIONS -> generateExamQuestions(schoolLevel, subject, term, topicDisplay)
            MaterialCategory.SCHEME_OF_WORK -> generateSchemeOfWork(schoolLevel, subject, term, topicDisplay)
            MaterialCategory.LEARNING_OBJECTIVES -> generateLearningObjectives(schoolLevel, subject, topicDisplay)
            MaterialCategory.MARKING_SCHEME -> generateMarkingScheme(schoolLevel, subject, topicDisplay)
            MaterialCategory.RESULT_GENERATOR -> generateResultComments(schoolLevel, subject, term, topicDisplay)
            MaterialCategory.QUIZ_GENERATOR -> generateQuiz(schoolLevel, subject, topicDisplay)
            MaterialCategory.NOTES_GENERATOR -> generateLessonNotes(schoolLevel, subject, term, topicDisplay)
            MaterialCategory.CURRICULUM_GUIDE -> generateCurriculumGuide(schoolLevel, subject, topicDisplay)
        }
    }

    private fun generateLessonPlan(schoolLevel: String, subject: String, term: String, topic: String): String {
        return """
# NERDC LESSON PLAN: $topic
**School Level:** $schoolLevel | **Subject:** $subject | **Term:** $term
**Duration:** 40 Minutes | **Date:** ___________________
**Topic:** $topic
**Sub-Topic:** Fundamental Principles & Applications

---

## 1. BEHAVIORAL OBJECTIVES
By the end of the lesson, the students should be able to:
1. **Define** $topic clearly using standard pedagogical terms.
2. **Identify and state** at least three key characteristics/components of $topic.
3. **Demonstrate understanding** by solving/analyzing practical problems related to $topic.
4. **Relate** $topic to real-life applications within the Nigerian society and environment.

---

## 2. INSTRUCTIONAL MATERIALS / TEACHING AIDS
- Realia, charts, diagrams, and chalkboard illustrations depicting $topic.
- Standard Nigerian textbook: NERDC / STAN approved series for $schoolLevel.
- Flashcards, models, or laboratory apparatus (where applicable).

---

## 3. ENTRY BEHAVIOR / PREVIOUS KNOWLEDGE
Students have previously encountered basic foundational concepts related to $subject in their prior lessons and are familiar with daily observations in Nigeria.

---

## 4. SET INDUCTION / INTRODUCTION (3-5 Minutes)
- **Teacher's Action:** Teacher greets the class, writes the date and topic on the board, and asks a thought-provoking introductory question: *"How does $topic affect our daily life or environment?"*
- **Learners' Action:** Students respond actively based on their background knowledge and real-life experiences.

---

## 5. INSTRUCTIONAL PROCEDURES / STEP-BY-STEP PRESENTATION (25 Minutes)

### Step 1: Conceptual Exploration & Definition (7 Mins)
- **Teacher:** Explains the core definition, historical context, and fundamental meaning of $topic.
- **Students:** Listen attentively, write key definitions in their notebooks, and ask clarifying questions.

### Step 2: In-Depth Explanation & Structure (8 Mins)
- **Teacher:** Breaks down the topic into sub-components, principles, formulas, and rules using the chalkboard and teaching aids.
- **Students:** Observe charts/demonstrations, participate in guided reading, and copy structured diagrams.

### Step 3: Worked Examples & Interactive Class Discussion (6 Mins)
- **Teacher:** Guides students through 2-3 standard examples/case studies illustrating $topic step-by-step.
- **Students:** Follow the steps, take notes, and contribute suggested solutions during guided exercises.

### Step 4: Supervised Practice & Group Activity (4 Mins)
- **Teacher:** Pairs up students to discuss a quick checkpoint problem on $topic.
- **Students:** Collaborate in pairs and volunteer answers to the class.

---

## 6. EVALUATION / FORMATIVE ASSESSMENT (5 Minutes)
The teacher assesses student understanding through the following oral/written questions:
1. What is the standard definition of $topic?
2. Mention two distinct features or classifications of $topic.
3. How can $topic be applied in our Nigerian community today?
4. Solve/Explain one practical problem on the board regarding $topic.

---

## 7. SUMMARY & CONCLUSION (2 Minutes)
- **Summary:** Teacher summarizes the major concepts of $topic, highlights common examination pitfalls, and commends student participation.
- **Conclusion:** Class note is verified and wrapped up.

---

## 8. TAKE-HOME ASSIGNMENT / HOMEWORK
1. Write a short 1-page summary explaining the significance of $topic in modern Nigerian society.
2. Answer review questions 1 to 5 on page 42 of the approved $subject textbook.
        """.trimIndent()
    }

    private fun generateExamQuestions(schoolLevel: String, subject: String, term: String, topic: String): String {
        return """
# $subject EXAMINATION ($term)
**Class:** $schoolLevel | **Time Allowed:** 1 Hour 30 Minutes | **Total Marks:** 100 Marks
**General Focus:** $topic and Termly Curriculum

---

### INSTRUCTIONS:
- Answer **ALL** questions in **Section A** (Objective Test).
- Answer any **THREE (3)** questions from **Section B** (Theory/Essay).

---

## SECTION A: OBJECTIVE TEST (40 MARKS)
*Choose the correct option from the alternatives lettered A to D.*

1. Which of the following best defines the primary concept of $topic?
   A. An unrelated phenomenon in $subject
   B. The fundamental systematic principle governing $topic
   C. A temporary observation with no scientific basis
   D. None of the above

2. In the study of $subject for $schoolLevel, $topic is primarily applied to:
   A. Increase complexity without practical use
   B. Solve real-world industrial, environmental, and civic challenges
   C. Replace foundational knowledge
   D. Eliminate the need for measurement

3. An essential characteristic of $topic is:
   A. Inconsistency under standard conditions
   B. Compliance with standard Nigerian curriculum benchmarks
   C. Inability to be measured or quantified
   D. Absence of structured rules

4. When analyzing $topic, the first systematic step a student should take is:
   A. Jump immediately to arbitrary conclusions
   B. State the given parameters and identify governing laws
   C. Ignore standard units and definitions
   D. Guess the final outcome

5. Which of the following is a direct benefit of mastering $topic in Nigeria?
   A. Promotes national technological and socio-economic development
   B. Slows down academic progression
   C. Has no relevance to WAEC or NECO examinations
   D. Restricts critical thinking

6. Identify the odd item related to $topic:
   A. Systematic observation
   B. Empirical data collection
   C. Unverified rumors
   D. Logical deduction

7. The standard unit or measure frequently utilized in $topic is:
   A. Arbitrary units
   B. Standard International (SI) units / standard metric units
   C. Non-standard estimates
   D. Hand-spans only

8. A major factor that influences the efficiency of $topic is:
   A. Environmental conditions and proper methodology
   B. Complete lack of supervision
   C. Random error proliferation
   D. Non-compliance with safety standards

9. In Nigerian history and development, $subject has played a key role by:
   A. Equipping youth with functional vocational and analytical skills
   B. Reducing educational literacy
   C. Eliminating industrial production
   D. Preventing scientific inquiry

10. Which branch of $subject directly incorporates $topic?
    A. Theoretical and Applied Studies
    B. Non-academic storytelling
    C. Arbitrary speculation
    D. Unregulated experimentation

---

## SECTION B: THEORY & ESSAY QUESTIONS (60 MARKS)
*Answer any THREE (3) questions from this section. All questions carry equal marks (20 Marks each).*

### QUESTION 1 (20 Marks)
(a) Define $topic and state TWO major conditions necessary for its operation. **[6 Marks]**
(b) With the aid of a neat, well-labeled diagram or structured breakdown, explain the key stages involved in $topic. **[8 Marks]**
(c) Highlight THREE practical applications of $topic in Nigerian industries or society. **[6 Marks]**

### QUESTION 2 (20 Marks)
(a) Differentiate clearly between the theoretical principles and practical manifestations of $topic. **[6 Marks]**
(b) A student was given an experiment/case study on $topic. State FOUR precautions they must observe to achieve accurate results. **[8 Marks]**
(c) Mention THREE limitations or challenges associated with $topic in developing nations. **[6 Marks]**

### QUESTION 3 (20 Marks)
(a) Outline the step-by-step procedure required to solve standard problems in $topic. **[6 Marks]**
(b) Calculate/Analyze the outcome when standard input parameters are varied by 25%. **[8 Marks]**
(c) State THREE career opportunities available in Nigeria for specialists in $subject. **[6 Marks]**

---

## ANSWER KEY & MARKING GUIDE (SECTION A)
1. **B** — Core definition of $topic.
2. **B** — Practical problem-solving application.
3. **B** — Compliance with standard benchmarks.
4. **B** — Systematic identification of parameters.
5. **A** — Socio-economic and national benefit.
6. **C** — Unverified claims are not scientific/academic.
7. **B** — Standard International SI Units.
8. **A** — Environmental and methodical factors.
9. **A** — Vocational and analytical skill acquisition.
10. **A** — Core theoretical and applied discipline.
        """.trimIndent()
    }

    private fun generateSchemeOfWork(schoolLevel: String, subject: String, term: String, topic: String): String {
        return """
# NERDC 12-WEEK SCHEME OF WORK
**School Level:** $schoolLevel | **Subject:** $subject | **Term:** $term
**Academic Session:** 2026/2027 | **Approved Standard:** NERDC / UBEC Syllabus

---

| Week | Curriculum Theme & Topic | Specific Behavioral Objectives | Teacher & Student Activities | Instructional Resources |
| :--- | :--- | :--- | :--- | :--- |
| **Wk 1** | **Orientation & Introduction to $topic** | 1. Define foundational terms.<br>2. Identify key concepts. | • Teacher introduces themes.<br>• Students take notes & brainstorm. | Charts, syllabus handbook, chalkboard. |
| **Wk 2** | **Core Principles & Classification** | 1. Classify the main types.<br>2. Differentiate characteristics. | • Teacher illustrates categories.<br>• Students participate in Q&A. | Realia, flashcards, diagrams. |
| **Wk 3** | **Detailed Structure & Components** | 1. List internal components.<br>2. Explain component functions. | • Teacher demonstrates models.<br>• Students draw and label diagrams. | Models, textbook illustrations. |
| **Wk 4** | **Processes, Formulas & Mechanisms** | 1. Apply rules/formulas.<br>2. Solve guided step problems. | • Teacher works sample calculations.<br>• Students solve practice questions. | Graph sheets, calculators, worksheets. |
| **Wk 5** | **Practical Demonstrations & Lab Work** | 1. Conduct hands-on tasks.<br>2. Record experimental data. | • Teacher supervises practicals.<br>• Students work in study groups. | Lab apparatus / local materials. |
| **Wk 6** | **MID-TERM BREAK & C.A. TEST** | 1. Assess Weeks 1-5 mastery.<br>2. Provide targeted feedback. | • Administer 30-min CA test.<br>• Review corrections with students. | Test question papers, answer keys. |
| **Wk 7** | **Advanced Applications & Case Studies** | 1. Evaluate complex scenarios.<br>2. Relate to Nigerian environment. | • Teacher leads case discussion.<br>• Students present group findings. | Case study handouts, audio-visuals. |
| **Wk 8** | **Environmental & Economic Impacts** | 1. Identify societal benefits.<br>2. Propose sustainable solutions. | • Debate on socio-economic impact.<br>• Students draft action points. | News articles, economic data charts. |
| **Wk 9** | **Problems, Limitations & Solutions** | 1. Analyze common pitfalls.<br>2. Formulate preventive steps. | • Problem-solving workshop.<br>• Peer-to-peer review sessions. | Problem cards, reference texts. |
| **Wk 10** | **Comprehensive Problem Sets** | 1. Solve WAEC/NECO/BECE past questions.<br>2. Master exam techniques. | • Timed drill practice.<br>• Teacher explains scoring rubric. | Past question compilations. |
| **Wk 11** | **General Revision & Tutorial Class** | 1. Revisit challenging topics.<br>2. Clarify all student doubts. | • Interactive revision clinic.<br>• Rapid-fire quiz competition. | Flashcards, summary mind-maps. |
| **Wk 12** | **TERMINAL EXAMINATION** | 1. Evaluate complete term syllabus. | • Administer Section A & B exam.<br>• Standardized invigilation. | Examination answer booklets. |
| **Wk 13** | **Marking, Results & Vacation** | 1. Collate scores & generate remarks. | • Compute CA & Exam percentages.<br>• Distribute report cards. | Master mark sheets, report books. |
        """.trimIndent()
    }

    private fun generateLearningObjectives(schoolLevel: String, subject: String, topic: String): String {
        return """
# BLOOM'S TAXONOMY LEARNING OBJECTIVES
**Subject:** $subject | **Class:** $schoolLevel
**Topic:** $topic

---

## 1. COGNITIVE DOMAIN (Intellectual & Thinking Skills)
1. **Remembering / Knowledge:**
   - Define $topic using accurate standard terminology.
   - List at least five key terms associated with $topic.
2. **Understanding / Comprehension:**
   - Explain the core operational mechanisms of $topic in student's own words.
   - Distinguish between primary and secondary attributes of $topic.
3. **Applying / Application:**
   - Use formulas and principles of $topic to solve standard quantitative/qualitative exercises.
   - Demonstrate how $topic solves everyday problems in Nigerian households or workplaces.
4. **Analyzing / Analysis:**
   - Break down complex processes of $topic into sequential constituent parts.
   - Compare and contrast different variations of $topic.
5. **Evaluating / Evaluation:**
   - Assess the validity of experimental results and identify potential systematic errors.
   - Defend conclusions using empirical data and logical reasoning.
6. **Creating / Synthesis:**
   - Design a miniature project, flowchart, or model demonstrating the practical application of $topic.

---

## 2. PSYCHOMOTOR DOMAIN (Practical & Hands-on Dexterity)
1. **Observation & Setup:** Accurately set up instructional materials or laboratory equipment needed for $topic.
2. **Execution & Manipulation:** Handle tools, instruments, or drawing apparatus cleanly and safely with minimal teacher guidance.
3. **Drafting & Labeling:** Draw neat, proportionate, and fully labeled biological/technical diagrams of $topic within 10 minutes.

---

## 3. AFFECTIVE DOMAIN (Attitudes, Values & Ethics)
1. **Active Participation:** Show enthusiasm, curiosity, and willingness to ask questions during classroom discussions on $topic.
2. **Teamwork & Collaboration:** Work harmoniously in peer groups while sharing instructional materials respectfully.
3. **Environmental & Ethical Consciousness:** Appreciate the impact of $topic on safety, conservation, and societal advancement in Nigeria.

---

## 4. SUCCESS CRITERIA RUBRIC
- **Distinction (80%+):** Mastered all cognitive levels including independent problem-solving and flawless diagrammatic labeling.
- **Credit (65-79%):** Demonstrates sound understanding, defines concepts accurately, solves basic to intermediate problems.
- **Pass (50-64%):** Recognizes foundational definitions with minor prompts; requires guidance on complex analysis.
        """.trimIndent()
    }

    private fun generateMarkingScheme(schoolLevel: String, subject: String, topic: String): String {
        return """
# OFFICIAL MARKING SCHEME & GRADING GUIDE
**Subject:** $subject | **Class:** $schoolLevel
**Assessment Topic:** $topic
**Standards Reference:** WAEC / NECO / BECE Assessment Rubric

---

## 1. MARK ALLOCATION LEGEND
- **B1 / B2:** Independent mark for correct statement of fact or definition.
- **M1 / M2:** Method mark for correct formula substitution or procedure.
- **A1 / A2:** Accuracy mark for exact numerical answer with correct standard units.
- **C1:** Conceptual/Concord mark for clear grammatical or technical expression.

---

## 2. DETAILED QUESTION-BY-QUESTION RUBRIC

### Question 1 (Total: 20 Marks)
- **(a) Definition of $topic:**
  - Correct, comprehensive definition mentioning governing principles = **[3 Marks]**
  - Partial definition omitting critical keywords = **[1 - 2 Marks]**
  - Two required conditions stated correctly (1.5 marks each) = **[3 Marks]**
- **(b) Step-by-Step Diagram / Mechanism:**
  - Neat title and proportionate drawing = **[2 Marks]**
  - Correct labeling of 4 major parts (1 mark each) = **[4 Marks]**
  - Quality of technical lines and arrows = **[2 Marks]**
- **(c) Three Nigerian Industrial / Practical Applications:**
  - Application 1 clearly explained = **[2 Marks]**
  - Application 2 clearly explained = **[2 Marks]**
  - Application 3 clearly explained = **[2 Marks]**

### Question 2 (Total: 20 Marks)
- **(a) Differentiating Principles:**
  - Tabulated or well-structured contrast points (3 pairs x 2 marks) = **[6 Marks]**
- **(b) Four Essential Precautions:**
  - Precaution 1 (e.g., zero error correction, eye level) = **[2 Marks]**
  - Precaution 2 (e.g., secure connections, calibration) = **[2 Marks]**
  - Precaution 3 = **[2 Marks]**
  - Precaution 4 = **[2 Marks]**
- **(c) Three Local Limitations:**
  - Stating 3 realistic infrastructural/environmental challenges (2 marks each) = **[6 Marks]**

---

## 3. COMMON STUDENT PITFALLS & PENALTIES
1. **Omission of Units:** Deduct **1 mark** if final numerical answers lack SI units (e.g., writing 50 instead of 50 kg or 50 m/s).
2. **Premature Rounding:** Deduct **1/2 mark** if intermediate values are rounded too early, causing significant divergence in final answer.
3. **Spelling of Key Terms:** Minor misspellings that do not change phonetic meaning are condoned; critical technical terms wrongly spelt lose **1 mark**.

---

## 4. NIGERIAN SECONDARY SCHOOL GRADING BENCHMARK
- **A1 (Distinction):** 75% - 100% (Excellent mastery)
- **B2 / B3 (Very Good / Good):** 65% - 74% (High proficiency)
- **C4 / C5 / C6 (Credit):** 50% - 64% (Standard passing credit)
- **D7 / E8 (Pass):** 40% - 49% (Needs remediation)
- **F9 (Fail):** 0% - 39% (Immediate intervention required)
        """.trimIndent()
    }

    private fun generateResultComments(schoolLevel: String, subject: String, term: String, topic: String): String {
        return """
# NIGERIAN REPORT CARD COMMENTS & PERFORMANCE REMARKS
**School Level:** $schoolLevel | **Subject:** $subject | **Term:** $term
**Assessment Focus:** Continuous Assessment (CA) & Terminal Exam on $topic

---

## 1. EXCELLENT / DISTINCTION PERFORMANCE (80% - 100% | Grade: A1)
- **Subject Teacher's Remark:** *"An exceptional and brilliant performance in $subject throughout the term. Demonstrates thorough mastery of $topic and solves complex exercises with outstanding precision. Keep up the high standard!"*
- **Class / Form Teacher's Remark:** *"A model student who consistently exhibits top-tier academic dedication, moral integrity, and exemplary leadership in class."*
- **Principal's / Head of School's Endorsement:** *"Outstanding result. A true pride to the school. Approved for commendation."*
- **Affective & Psychomotor Traits:**
  - Punctuality & Attendance: **A (Excellent)**
  - Neatness & Personal Hygiene: **A (Excellent)**
  - Leadership & Initiative: **A (Excellent)**
  - Politeness & Obedience: **A (Excellent)**

---

## 2. VERY GOOD / CREDIT PERFORMANCE (65% - 79% | Grade: B2 - B3)
- **Subject Teacher's Remark:** *"A very commendable result in $subject. Has a solid grasp of $topic and contributes actively during lessons. With slightly more attention to worked step details, will easily attain an A1 distinction."*
- **Class / Form Teacher's Remark:** *"A diligent, well-behaved, and hardworking student. Shows consistent effort across all school activities."*
- **Principal's Endorsement:** *"Good result with clear potential for distinction next term."*

---

## 3. AVERAGE / CREDIT PERFORMANCE (50% - 64% | Grade: C4 - C6)
- **Subject Teacher's Remark:** *"A satisfactory performance, but there is substantial room for improvement in $subject, especially in topics like $topic. Regular practice and timely homework submission are strongly advised."*
- **Class / Form Teacher's Remark:** *"Fair performance. Needs to minimize classroom distractions and concentrate more on personal revision."*
- **Principal's Endorsement:** *"Promising, but requires greater discipline and steady study habits."*

---

## 4. STRUGGLING / NEEDS REMEDIATION (Below 50% | Grade: D7 - F9)
- **Subject Teacher's Remark:** *"Weak performance in $subject. Struggled to comprehend fundamental concepts of $topic. Immediate remedial tutoring, active participation, and targeted holiday revision are strongly recommended."*
- **Class / Form Teacher's Remark:** *"Lacks academic focus and frequently fails to submit assignments on time. Parents are urgently requested to supervise study routines at home."*
- **Principal's Endorsement:** *"Urgent parental intervention and remedial coaching required."*

---

## 5. QUICK-COPY REMARKS BANK FOR NIGERIAN TEACHERS
1. *"An eager learner with sharp analytical instincts in $subject."*
2. *"Maintains neat notes and actively participates in classroom discussions."*
3. *"Capable of far better results if consistent home study is maintained."*
4. *"A well-mannered pupil who takes corrections constructively."*
5. *"Exhibits commendable leadership qualities and assists peers during group work."*
        """.trimIndent()
    }

    private fun generateQuiz(schoolLevel: String, subject: String, topic: String): String {
        return """
# CLASSROOM POP QUIZ: $topic
**School Level:** $schoolLevel | **Subject:** $subject
**Duration:** 15 Minutes | **Total Marks:** 20 Marks

---

### INSTRUCTIONS:
Answer all questions. For Questions 1 to 7, choose the correct letter (A-D). For Questions 8 to 10, write short concise answers.

---

## QUESTIONS:

1. What is the fundamental definition of $topic in $subject?
   A. A temporary hypothesis without evidence
   B. The established principle and core process governing $topic
   C. An obsolete method not used in Nigeria
   D. None of the above

2. Which of the following is an indispensable component or stage of $topic?
   A. Initial observation and parameter definition
   B. Random guesswork
   C. Disregarding safety regulations
   D. Skipping foundational steps

3. In Nigerian schools, why is $topic taught in $schoolLevel?
   A. To develop analytical, scientific, and functional problem-solving skills
   B. To increase exam difficulty unnecessarily
   C. To avoid modern technological learning
   D. Only for rote memorization

4. When conducting a classwork test on $topic, which factor must be kept constant?
   A. Controlled variables and measurement standards
   B. Random background noise
   C. Arbitrary unit changes
   D. Guesswork

5. Which tool or teaching aid is best suited to demonstrate $topic?
   A. Concrete models, charts, and approved NERDC textbook diagrams
   B. Unverified internet rumors
   C. Fictional comic books
   D. None of the above

6. What is the standard consequence of applying the rules of $topic incorrectly?
   A. Flawed results and inaccurate calculations
   B. Automatic bonus marks
   C. Instant mastery
   D. No effect whatsoever

7. Identify the primary beneficiary of mastering $topic in our community:
   A. The society, through improved productivity, health, and technology
   B. Nobody
   C. Only foreign nations
   D. Malfunctioning equipment

8. *(Fill in the blank)* The standard SI unit or primary term used to measure the rate or magnitude of $topic is _______________.

9. *(Short Answer)* State TWO distinct safety rules or precautions when studying or experimenting with $topic.

10. *(Short Answer)* Give ONE real-life example of where $topic is seen in action in Nigeria today.

---

## ANSWER KEY & EXPLANATIONS (FOR TEACHER)
1. **B** — Core textbook definition.
2. **A** — Foundational systematic step.
3. **A** — NERDC educational objective.
4. **A** — Standard experimental control.
5. **A** — Concrete realia and diagrams.
6. **A** — Inaccurate procedure leads to flawed conclusions.
7. **A** — Societal and technological empowerment.
8. **Answer:** Standard International (SI) units / relevant specific metric unit.
9. **Answer:** 1. Ensuring careful calibration of tools. 2. Avoiding contamination or handling errors.
10. **Answer:** Nigerian agricultural practices, manufacturing plants, telecommunication networks, or public health sanitation.

### BONUS CHALLENGE QUESTION:
*Explain in 2 sentences how the concept of $topic will change with the integration of modern artificial intelligence and digital technology.*
        """.trimIndent()
    }

    private fun generateLessonNotes(schoolLevel: String, subject: String, term: String, topic: String): String {
        return """
# CHALKBOARD LESSON NOTE: $topic
**Subject:** $subject | **Class:** $schoolLevel | **Term:** $term
**Topic:** $topic
**Sub-Topic:** Definitions, Properties, Rules & Worked Examples

---

## 1. INTRODUCTION & DEFINITION
**$topic** is defined as the systematic study, process, or principle in $subject whereby specific inputs, rules, or phenomena interact to produce structured, predictable outcomes.

In the Nigerian educational curriculum, understanding $topic provides students with foundational mastery required for WAEC, NECO, and everyday problem-solving.

---

## 2. KEY PROPERTIES & CHARACTERISTICS
The primary features of $topic include:
- **Property 1:** It follows established universal and empirical laws.
- **Property 2:** It can be categorized into distinct classes or sub-types based on structure and function.
- **Property 3:** It directly interacts with environmental and socio-economic systems.
- **Property 4:** Quantitative aspects must always be accompanied by standard metric (SI) units.

---

## 3. DETAILED BREAKDOWN & STEP-BY-STEP RULES
When analyzing or working with $topic, follow the 4-step framework:

1. **Step 1 — Identification:** Read and write down all given parameters, boundary conditions, or definitions.
2. **Step 2 — Formula / Theoretical Model Selection:** Choose the appropriate NERDC-approved formula or model.
3. **Step 3 — Substitution & Execution:** Substitute values or follow the analytical procedure methodically.
4. **Step 4 — Verification & Units:** Double check the answer for sanity and attach the proper units.

---

## 4. WORKED EXAMPLES (BOARD WORK)

### Example 1 (Foundational):
*Problem:* State the main difference between Type A and Type B in $topic.
*Solution:*
- **Type A:** Operates under standard conditions with direct proportionality.
- **Type B:** Requires external activation energy or catalytic inputs.
- *Conclusion:* Type A is spontaneous, whereas Type B is non-spontaneous.

### Example 2 (Computational / Practical):
*Problem:* Given an initial quantity of 100 units at a growth/processing rate of 15%, calculate the final yield.
*Solution:*
$$\text{Final Yield} = \text{Initial} \times (1 + \text{Rate})$$
$$\text{Final Yield} = 100 \times (1 + 0.15) = 100 \times 1.15 = 115\text{ units}$$
*Final Answer:* **115 units**.

---

## 5. SUMMARY POINTS FOR STUDENT NOTEBOOK
- **Definition:** $topic is essential in $subject for analysis and problem-solving.
- **Key Formula/Rule:** Always follow structured steps and verify SI units.
- **Local Application:** Used extensively across Nigerian agriculture, engineering, healthcare, and business.

---

## 6. CLASSWORK EXERCISE (10 Minutes)
*Instructions: Copy into your 40-leaves exercise book and answer immediately.*
1. Define $topic in two sentences.
2. State two industrial applications of $topic in Nigeria.
3. Solve: If input parameters increase by 20%, what is the expected outcome on total efficiency?
4. Write down the major safety rule to observe during practical sessions.
        """.trimIndent()
    }

    private fun generateCurriculumGuide(schoolLevel: String, subject: String, topic: String): String {
        return """
# NERDC NATIONAL CURRICULUM SYLLABUS GUIDE
**Subject:** $subject | **Target Level:** $schoolLevel
**Core Focus Area:** $topic
**Governing Body:** Nigerian Educational Research & Development Council (NERDC)

---

## 1. NATIONAL CURRICULUM PHILOSOPHY & OBJECTIVES
The Nigerian National Curriculum for $subject at the $schoolLevel stage is engineered to:
1. Provide functional literacy, numeracy, and scientific/technological inquiry.
2. Cultivate critical thinking, moral values, and entrepreneurial initiative in Nigerian learners.
3. Prepare students thoroughly for national and regional examinations (WAEC, NECO, BECE, NABTEB, JAMB UTME).

---

## 2. CORE THEMATIC MODULES

### Module A: Foundations & Principles
- Introduction to basic terminology and scope.
- Fundamental safety and laboratory / workshop guidelines.

### Module B: Thematic Mastery & $topic
- In-depth study of $topic across progressive weeks.
- Practical experiments, fieldwork, and local case studies.

### Module C: Applications to Nigerian Society
- Value addition to local raw materials, agro-allied industries, and civic governance.
- Environmental protection and climate resilience.

---

## 3. APPROVED RECOMMENDED TEXTBOOKS IN NIGERIA
1. **STAN (Science Teachers Association of Nigeria)** Approved Series.
2. **MAN (Mathematical Association of Nigeria)** Curriculum Guides.
3. **Learn Africa / Evans Brothers / Extension Publishers** Standard Textbooks.
4. **Macmillan / Melrose Nigerian Secondary Series**.

---

## 4. WAEC / NECO / BECE EXAM STRATEGY & HIGH-YIELD TOPICS
- **Paper 1 (Objectives):** Fast-paced conceptual recall (60 questions in 60 mins).
- **Paper 2 (Essay/Theory):** Structured multi-part questions requiring clear steps, correct SI units, and fully labeled diagrams.
- **Paper 3 (Practicals / Alternative to Practical):** Accurate readings, table of observations, gradient calculations, and precaution statements.
        """.trimIndent()
    }
}
