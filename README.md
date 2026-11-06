# TeacherMate NG 🇳🇬

> **The AI Assistant for Nigerian Educators** — Designed for Primary, JSS, and SSS teachers to rapidly generate lesson plans, examination questions, schemes of work, lesson notes, and rubrics aligned with the official Nigerian Educational Research and Development Council (NERDC) curriculum standards.

[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0+-purple.svg)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-blue.svg)](https://developer.android.com/jetpack/compose)
[![NERDC Aligned](https://img.shields.io/badge/Curriculum-NERDC%20Nigeria-006C4C.svg)](https://nerdc.org.ng)
[![Powered by Gemini](https://img.shields.io/badge/AI-Gemini%20Flash-orange.svg)](https://ai.google.dev)

---

## 🌟 Key Features

- **📚 8-Step NERDC Lesson Plan Generator**:
  - Automatically structures lesson plans according to national Nigerian educational conventions:
    1. Administrative & Class Details
    2. Behavioral Objectives (Cognitive, Affective, Psychomotor using Bloom's Revised Taxonomy)
    3. Instructional Materials & Local Nigerian Teaching Aids (Realia)
    4. Previous Knowledge / Entry Behavior
    5. Set Induction (Hook)
    6. Instructional Procedures (Step 1–4 with Teacher & Student Activities)
    7. Formative Assessment & Evaluation Questions
    8. Summary, Conclusion & Homework Assignment
  - Multi-stage grade selector: Kindergarten, Primary 1–6, JSS 1–3, SSS 1–3 with age brackets.
  - Interactive Gemini-powered prompt refinement pills (*Add local realia*, *Include group activity*, *Differentiate for struggling learners*).

- **📝 WAEC / NECO / BECE-Ready Exam Question Generator**:
  - Multiple choice (with standard 4/5 options and answer keys) and structured essay theory questions.
  - Bloom's cognitive domain classification (Recall, Comprehension, Application, Analysis).

- **📖 Scheme of Work & Syllabus Breakdown**:
  - Termly breakdown (1st, 2nd, 3rd Term) across 10–12 teaching weeks including mid-term review and revision weeks.

- **✅ Objective Marking Schemes & Rubrics**:
  - Point-by-point allocation rubrics for exams and classroom assessments.

- **📄 Classroom Summary Notes**:
  - Chalkboard-friendly structured bullet notes with definitions, worked examples, and key takeaways.

- **📥 Dual-Mode & Export**:
  - Works online with **Gemini AI API** (`gemini-2.5-flash`) and includes an automatic fallback to an **offline NERDC curriculum template engine** when no internet connectivity is available.
  - Export generated materials as formatted text, printable layout, or share directly via WhatsApp, Email, or Google Classroom.

---

## 🏗️ Architecture & Tech Stack

- **UI Framework**: Modern Jetpack Compose with Material 3 (M3).
- **Architecture**: MVVM (Model-View-ViewModel) with Kotlin Coroutines and `StateFlow`.
- **Local Persistence**: Room Database for saving created lesson plans, exams, and schemes locally on the device.
- **AI Integration**: Gemini API REST integration with secure key handling via Secrets Gradle Plugin.
- **Testing**: Robolectric JVM unit and screenshot testing.

---

## 🚀 Getting Started

### Prerequisites

- **Android Studio**: Android Studio Koala Feature Drop, Ladybug, or newer.
- **JDK**: Java Development Kit (JDK) 17 or 21.
- **Android SDK**: Compile SDK 36 (target SDK 36, min SDK 24 — Android 7.0+).

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/TeacherMate-NG.git
cd TeacherMate-NG
```

### 2. Configure API Keys (Optional for Online AI)

The app features offline template generation out-of-the-box. To enable live Gemini AI generation:

1. Obtain a free Gemini API key from [Google AI Studio](https://aistudio.google.com/).
2. Create a `.env` file in the root directory of the project (this file is git-ignored):
   ```properties
   GEMINI_API_KEY=your_actual_gemini_api_key_here
   ```
3. When building, the Secrets Gradle Plugin will inject this key into `BuildConfig.GEMINI_API_KEY`.

### 3. Build & Run

#### Using Android Studio:
1. Open Android Studio and select **Open** -> Select the project root folder.
2. Wait for Gradle Sync to complete.
3. Select an emulator or connected physical Android device.
4. Click **Run ▶** (`Shift + F10`).

#### Using the Command Line:
```bash
# Build the Debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew testDebugUnitTest
```
The compiled APK will be located at:
`app/build/outputs/apk/debug/app-debug.apk`

---

## 📂 Project Structure

```text
├── app/
│   ├── src/main/java/com/example/
│   │   ├── ai/                 # Gemini AI Service & Offline Curriculum Templates
│   │   ├── data/               # Room Database, DAOs, and Entities
│   │   ├── model/              # NERDC Curriculum & Education Models
│   │   ├── ui/                 # Jetpack Compose Screens, Components & Themes
│   │   │   ├── screens/        # LessonPlanGeneratorScreen, ExamGenScreen, etc.
│   │   │   ├── components/     # TopAppBar, BottomNav, Dialogs, Cards
│   │   │   └── theme/          # M3 Colors, Typography, Shapes
│   │   └── viewmodel/          # TeacherMateViewModel (StateFlow & Business Logic)
│   ├── src/test/               # Robolectric & JUnit unit tests
│   └── build.gradle.kts        # App dependencies & Android build config
├── gradle/
│   ├── wrapper/                # Gradle wrapper binaries & properties
│   └── libs.versions.toml      # Gradle Version Catalog
├── .env.example                # Template for Gemini API secrets
├── .gitignore                  # Git ignore rules for Android projects
└── README.md                   # Project documentation
```

---

## 📄 License & Attribution

Designed and built for Nigerian educators. Curriculum structure adheres to the guidelines established by the **Nigerian Educational Research and Development Council (NERDC)**.
