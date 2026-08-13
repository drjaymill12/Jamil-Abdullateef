package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.EducationLevel
import com.example.model.MaterialCategory
import com.example.model.NigerianCurriculumData
import com.example.ui.theme.BorderCard
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.ChipSelectedBg
import com.example.ui.theme.ChipSelectedBorder
import com.example.ui.theme.ChipSelectedText
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.TextSecondaryLight

@Composable
fun CurriculumHubScreen(
    onGenerateForTopic: (category: MaterialCategory, level: EducationLevel, subject: String, topic: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategoryLevel by remember { mutableStateOf("Junior Secondary") }
    var selectedSubject by remember(selectedCategoryLevel) {
        val list = NigerianCurriculumData.SUBJECTS_BY_LEVEL[selectedCategoryLevel] ?: listOf("Mathematics")
        mutableStateOf(list.firstOrNull() ?: "Mathematics")
    }

    val availableSubjects = NigerianCurriculumData.SUBJECTS_BY_LEVEL[selectedCategoryLevel] ?: emptyList()
    val availableTopics = NigerianCurriculumData.TOPIC_SUGGESTIONS[selectedSubject]
        ?: listOf(
            "Introduction to $selectedSubject Concepts",
            "Classification & Key Terminology",
            "Theoretical Models & Mechanisms",
            "Practical Demonstrations & Local Applications",
            "Evaluation & National Exam Preparations"
        )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("curriculum_hub_screen"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Hero Header Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = GreenPrimary),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🇳🇬", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "NERDC National Curriculum Explorer",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Browse standard syllabus modules from Kindergarten to SS3. Tap any topic to compile lesson plans, exams, notes, or schemes of work.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        )
                    }
                }
            }
        }

        // Level Selector Tabs
        item {
            Text(
                text = "1. Select Education Stage",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val stages = listOf(
                    "Kindergarten" to "👶 Kindergarten",
                    "Primary" to "🎒 Primary (1-6)",
                    "Junior Secondary" to "🏫 JSS (1-3)",
                    "Senior Secondary" to "🎓 SSS (1-3)"
                )
                stages.forEach { (catKey, label) ->
                    val isSelected = selectedCategoryLevel == catKey
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategoryLevel = catKey },
                        label = {
                            Text(
                                label,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ChipSelectedBg,
                            selectedLabelColor = ChipSelectedText,
                            containerColor = Color.White,
                            labelColor = TextSecondaryLight
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            selectedBorderColor = ChipSelectedBorder,
                            borderColor = BorderSubtle
                        )
                    )
                }
            }
        }

        // Subject Selector
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "2. Approved Nigerian Subjects",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                availableSubjects.forEach { subj ->
                    val isSelected = selectedSubject == subj
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedSubject = subj },
                        label = {
                            Text(
                                subj,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                            )
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ChipSelectedBg,
                            selectedLabelColor = ChipSelectedText,
                            containerColor = Color.White,
                            labelColor = TextSecondaryLight
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            selectedBorderColor = ChipSelectedBorder,
                            borderColor = BorderSubtle
                        )
                    )
                }
            }
        }

        // Topic Breakdown
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Core NERDC Syllabus Topics (${availableTopics.size})",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$selectedCategoryLevel • $selectedSubject",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        color = TextSecondaryLight
                    )
                )
            }
        }

        items(availableTopics) { topic ->
            CurriculumTopicCard(
                topic = topic,
                subject = selectedSubject,
                stage = selectedCategoryLevel,
                onGenerateLessonPlan = {
                    val sampleLevel = when (selectedCategoryLevel) {
                        "Kindergarten" -> EducationLevel.KG_1
                        "Primary" -> EducationLevel.PRIMARY_4
                        "Junior Secondary" -> EducationLevel.JSS_2
                        else -> EducationLevel.SS_2
                    }
                    onGenerateForTopic(MaterialCategory.LESSON_PLAN, sampleLevel, selectedSubject, topic)
                },
                onGenerateExam = {
                    val sampleLevel = when (selectedCategoryLevel) {
                        "Kindergarten" -> EducationLevel.KG_1
                        "Primary" -> EducationLevel.PRIMARY_4
                        "Junior Secondary" -> EducationLevel.JSS_2
                        else -> EducationLevel.SS_2
                    }
                    onGenerateForTopic(MaterialCategory.EXAM_QUESTIONS, sampleLevel, selectedSubject, topic)
                },
                onGenerateNotes = {
                    val sampleLevel = when (selectedCategoryLevel) {
                        "Kindergarten" -> EducationLevel.KG_1
                        "Primary" -> EducationLevel.PRIMARY_4
                        "Junior Secondary" -> EducationLevel.JSS_2
                        else -> EducationLevel.SS_2
                    }
                    onGenerateForTopic(MaterialCategory.NOTES_GENERATOR, sampleLevel, selectedSubject, topic)
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun CurriculumTopicCard(
    topic: String,
    subject: String,
    stage: String,
    onGenerateLessonPlan: () -> Unit,
    onGenerateExam: () -> Unit,
    onGenerateNotes: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp),
        border = BorderStroke(1.dp, BorderCard)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = GreenPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = topic,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "NERDC Benchmark • Recommended for $stage $subject",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.sp,
                    color = TextSecondaryLight
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Action Pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ChipSelectedBg,
                    border = BorderStroke(1.dp, ChipSelectedBorder),
                    modifier = Modifier.clickable { onGenerateLessonPlan() }
                ) {
                    Text(
                        text = "📚 Lesson Plan",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = ChipSelectedText,
                            fontSize = 11.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ChipSelectedBg,
                    border = BorderStroke(1.dp, ChipSelectedBorder),
                    modifier = Modifier.clickable { onGenerateExam() }
                ) {
                    Text(
                        text = "📝 Exam",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = ChipSelectedText,
                            fontSize = 11.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ChipSelectedBg,
                    border = BorderStroke(1.dp, ChipSelectedBorder),
                    modifier = Modifier.clickable { onGenerateNotes() }
                ) {
                    Text(
                        text = "📄 Notes",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = ChipSelectedText,
                            fontSize = 11.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                    )
                }
            }
        }
    }
}
