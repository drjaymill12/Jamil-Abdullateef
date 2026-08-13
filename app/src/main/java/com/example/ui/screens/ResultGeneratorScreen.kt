package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SavedMaterial
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GreenPrimary

@Composable
fun ResultGeneratorScreen(
    onSaveMaterial: (SavedMaterial) -> Unit,
    onExportPdf: (SavedMaterial) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var studentName by remember { mutableStateOf("Chidiebere Okafor") }
    var className by remember { mutableStateOf("JSS 2 Gold") }
    var subjectName by remember { mutableStateOf("Basic Science") }
    var caScoreInput by remember { mutableStateOf("28") }
    var examScoreInput by remember { mutableStateOf("54") }

    val caScore = caScoreInput.toIntOrNull() ?: 0
    val examScore = examScoreInput.toIntOrNull() ?: 0
    val totalScore = (caScore + examScore).coerceIn(0, 100)

    val (grade, gradeRemark) = when {
        totalScore >= 75 -> "A1 (Distinction)" to "Excellent Mastery"
        totalScore >= 70 -> "B2 (Very Good)" to "Very Good Proficiency"
        totalScore >= 65 -> "B3 (Good)" to "Good Understanding"
        totalScore >= 60 -> "C4 (Credit)" to "Credit Pass"
        totalScore >= 55 -> "C5 (Credit)" to "Credit Pass"
        totalScore >= 50 -> "C6 (Credit)" to "Pass Credit"
        totalScore >= 45 -> "D7 (Pass)" to "Fair Pass"
        totalScore >= 40 -> "E8 (Pass)" to "Weak Pass"
        else -> "F9 (Fail)" to "Needs Urgent Remediation"
    }

    var generatedRemarkResult by remember { mutableStateOf<String?>(null) }

    fun computeRemarks() {
        val teacherRemark = when {
            totalScore >= 80 -> "An exceptional and brilliant performance in $subjectName throughout the term. Demonstrates thorough mastery and solves complex problems with ease. Keep up the high standard!"
            totalScore >= 70 -> "A very commendable result in $subjectName. Has a solid grasp of concepts and participates actively in class. Has the potential to achieve an A1 distinction with focused revision."
            totalScore >= 60 -> "A good, steady performance. Shows consistent effort in assignments, but needs to pay closer attention to worked step details and practical questions."
            totalScore >= 50 -> "A fair performance. Has basic understanding, but requires regular practice and prompt homework submission to attain higher credit grades."
            else -> "A weak performance in $subjectName. Struggled with foundational concepts. Immediate remedial lessons and strict parental supervision of study routines are strongly recommended."
        }

        val formTeacherRemark = when {
            totalScore >= 70 -> "A highly disciplined, well-behaved, and industrious student. Exemplary conduct."
            totalScore >= 50 -> "A calm and obedient student. Can achieve greater academic heights with sustained focus."
            else -> "Needs to show greater seriousness and minimize classroom distractions."
        }

        val principalEndorsement = when {
            totalScore >= 75 -> "Outstanding result. Approved for commendation."
            totalScore >= 50 -> "Satisfactory progress. Encouraged to aim higher next term."
            else -> "Academic performance requires urgent intervention and parental review."
        }

        generatedRemarkResult = """
# NIGERIAN TERMLY REPORT SHEET
**Student Name:** $studentName
**Class:** $className | **Subject:** $subjectName
**Session:** 2026/2027 Academic Session

---

## 1. ACADEMIC SCORES & ASSESSMENT
- **Continuous Assessment (C.A. 40%):** $caScore / 40
- **Terminal Examination (60%):** $examScore / 60
- **TOTAL PERCENTAGE:** **$totalScore%**
- **OFFICIAL WAEC/NERDC GRADE:** **$grade**
- **PERFORMANCE CLASSIFICATION:** $gradeRemark

---

## 2. OFFICIAL TEACHER & PRINCIPAL REMARKS
- **Subject Teacher's Remark:**
  "$teacherRemark"

- **Class / Form Teacher's Remark:**
  "$formTeacherRemark"

- **Principal's / Head of School's Endorsement:**
  "$principalEndorsement"

---

## 3. AFFECTIVE & BEHAVIORAL TRAITS
- Punctuality & Regularity: **${if (totalScore >= 60) "A (Excellent)" else "B (Good)"}**
- Neatness & Uniform: **A (Excellent)**
- Politeness & Respect: **A (Excellent)**
- Class Participation: **${if (totalScore >= 60) "A (Active)" else "B (Satisfactory)"}**
        """.trimIndent()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("result_generator_screen"),
        contentPadding = PaddingValues(16.dp, bottom = 80.dp)
    ) {
        item {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("📊", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Result & Report Card Remarks Generator",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    )
                    Text(
                        text = "Nigerian Grading Standard (WAEC / NERDC Rubric)",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Input Form Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = studentName,
                        onValueChange = { studentName = it },
                        label = { Text("Student Full Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("result_student_name"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = className,
                            onValueChange = { className = it },
                            label = { Text("Class (e.g. JSS 2)") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("result_class_name"),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = subjectName,
                            onValueChange = { subjectName = it },
                            label = { Text("Subject") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("result_subject_name"),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // CA & Exam Score Inputs
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = caScoreInput,
                            onValueChange = { caScoreInput = it },
                            label = { Text("C.A. Score (max 40)") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("result_ca_score"),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = examScoreInput,
                            onValueChange = { examScoreInput = it },
                            label = { Text("Exam Score (max 60)") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("result_exam_score"),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Live Calculated Score Card
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Total: $totalScore% ($gradeRemark)",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "Grade: $grade",
                                    style = MaterialTheme.typography.bodySmall.copy(color = GreenPrimary, fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { computeRemarks() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("compute_result_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GreenPrimary,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Generate Professional Remarks", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Generated Output
        val resultText = generatedRemarkResult
        if (resultText != null) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Generated Remarks & Report",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GreenPrimary
                                )
                            )

                            Row {
                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(resultText))
                                        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                                    }
                                ) {
                                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy")
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = resultText,
                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Save & PDF Export
                        val materialToSave = SavedMaterial(
                            category = "RESULT_GENERATOR",
                            title = "Report Sheet: $studentName ($className)",
                            educationLevel = className,
                            subject = subjectName,
                            term = "Termly Assessment",
                            topic = "Performance & Remarks",
                            content = resultText
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    onExportPdf(materialToSave)
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                            ) {
                                Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("PDF Report", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            FilledTonalButton(
                                onClick = {
                                    onSaveMaterial(materialToSave)
                                    Toast.makeText(context, "Saved to library", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Save Card", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
