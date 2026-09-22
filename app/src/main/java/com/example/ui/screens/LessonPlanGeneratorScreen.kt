package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SavedMaterial
import com.example.model.EducationLevel
import com.example.model.NigerianCurriculumData
import com.example.model.SchoolTerm
import com.example.ui.theme.BadgeAmber
import com.example.ui.theme.BadgeBlue
import com.example.ui.theme.BadgeEmerald
import com.example.ui.theme.BorderCard
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.ChipSelectedBg
import com.example.ui.theme.ChipSelectedBorder
import com.example.ui.theme.ChipSelectedText
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.SurfaceLight
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.viewmodel.TeacherMateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonPlanGeneratorScreen(
    viewModel: TeacherMateViewModel,
    onViewFullDocument: (SavedMaterial) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val subject by viewModel.lessonPlanSubject.collectAsState()
    val grade by viewModel.lessonPlanGrade.collectAsState()
    val topic by viewModel.lessonPlanTopic.collectAsState()
    val subTopic by viewModel.lessonPlanSubTopic.collectAsState()
    val term by viewModel.lessonPlanTerm.collectAsState()
    val duration by viewModel.lessonPlanDuration.collectAsState()
    val aids by viewModel.lessonPlanAids.collectAsState()
    val customNotes by viewModel.lessonPlanCustomNotes.collectAsState()

    val activePlan by viewModel.activeLessonPlan.collectAsState()
    val isGenerating by viewModel.isGeneratingPlan.collectAsState()
    val progressStep by viewModel.lessonPlanProgressStep.collectAsState()
    val aiNotice by viewModel.lessonPlanAiNotice.collectAsState()
    val errorMessage by viewModel.lessonPlanError.collectAsState()

    var showAdvancedOptions by remember { mutableStateOf(false) }
    var resultViewMode by remember { mutableIntStateOf(0) } // 0 = Structured Sections, 1 = Raw Editable Text
    var editableContent by remember(activePlan?.content) { mutableStateOf(activePlan?.content ?: "") }
    var refinementPrompt by remember { mutableStateOf("") }

    // Subject dropdown state
    var isSubjectDropdownExpanded by remember { mutableStateOf(false) }
    // Grade dropdown state
    var isGradeDropdownExpanded by remember { mutableStateOf(false) }

    // Stage selection for quick filtering (Kindergarten, Primary, Junior Secondary, Senior Secondary)
    var selectedStage by remember(grade) { mutableStateOf(grade.category) }

    val stageSubjects = NigerianCurriculumData.SUBJECTS_BY_LEVEL[selectedStage] ?: emptyList()
    val topicSuggestions = NigerianCurriculumData.TOPIC_SUGGESTIONS[subject] ?: emptyList()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceLight)
            .testTag("lesson_plan_generator_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Top Header Banner
        item {
            Surface(
                color = GreenPrimary,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.2f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("🇳🇬", fontSize = 18.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Lesson Plan Generator",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp
                                    )
                                )
                                Text(
                                    text = "Standard 8-Step NERDC Format • Gemini AI",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color.White.copy(alpha = 0.85f),
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }

                        // Reset / New button
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.clickable {
                                viewModel.clearLessonPlanInputs()
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "New Plan",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "New",
                                    color = Color.White,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 1: Teacher Inputs Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .testTag("lesson_plan_inputs_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, BorderCard),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Header label
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = ChipSelectedBg,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("1", fontWeight = FontWeight.Bold, color = GreenPrimary, fontSize = 12.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Lesson Specifications",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Education Stage Filter Chips
                    Text(
                        text = "EDUCATION STAGE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondaryLight,
                            letterSpacing = 0.5.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val stages = listOf(
                            "Kindergarten" to "👶 Kindergarten",
                            "Primary" to "🎒 Primary (1-6)",
                            "Junior Secondary" to "🏫 JSS (1-3)",
                            "Senior Secondary" to "🎓 SSS (1-3)"
                        )
                        stages.forEach { (catKey, label) ->
                            val isSelected = selectedStage == catKey
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    selectedStage = catKey
                                    // pick default grade for this stage
                                    val defaultGrade = when (catKey) {
                                        "Kindergarten" -> EducationLevel.KG_1
                                        "Primary" -> EducationLevel.PRIMARY_4
                                        "Junior Secondary" -> EducationLevel.JSS_2
                                        else -> EducationLevel.SS_2
                                    }
                                    viewModel.setLessonPlanGrade(defaultGrade)
                                    // pick default subject for this stage
                                    val newStageSubjs = NigerianCurriculumData.SUBJECTS_BY_LEVEL[catKey] ?: emptyList()
                                    if (newStageSubjs.isNotEmpty() && !newStageSubjs.contains(subject)) {
                                        viewModel.setLessonPlanSubject(newStageSubjs.first())
                                    }
                                },
                                label = { Text(label, fontSize = 11.5.sp) },
                                shape = RoundedCornerShape(18.dp),
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

                    Spacer(modifier = Modifier.height(12.dp))

                    // Class / Grade Selector Dropdown
                    Text(
                        text = "GRADE / CLASS LEVEL",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondaryLight,
                            letterSpacing = 0.5.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    val gradeOptionsForStage = EducationLevel.values().filter { it.category == selectedStage }

                    ExposedDropdownMenuBox(
                        expanded = isGradeDropdownExpanded,
                        onExpandedChange = { isGradeDropdownExpanded = !isGradeDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = "${grade.displayName} (${grade.ageGroup})",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isGradeDropdownExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .testTag("grade_selector_input"),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GreenPrimary,
                                unfocusedBorderColor = BorderCard
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = isGradeDropdownExpanded,
                            onDismissRequest = { isGradeDropdownExpanded = false }
                        ) {
                            gradeOptionsForStage.forEach { opt ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(opt.displayName, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                            Text(opt.ageGroup, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryLight, fontSize = 11.sp))
                                        }
                                    },
                                    onClick = {
                                        viewModel.setLessonPlanGrade(opt)
                                        isGradeDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Subject Selector & Quick Chips
                    Text(
                        text = "APPROVED NIGERIAN SUBJECT",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondaryLight,
                            letterSpacing = 0.5.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    ExposedDropdownMenuBox(
                        expanded = isSubjectDropdownExpanded,
                        onExpandedChange = { isSubjectDropdownExpanded = !isSubjectDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = subject,
                            onValueChange = { viewModel.setLessonPlanSubject(it) },
                            placeholder = { Text("e.g., Mathematics, English Language, Basic Science") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isSubjectDropdownExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .testTag("subject_input"),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GreenPrimary,
                                unfocusedBorderColor = BorderCard
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = isSubjectDropdownExpanded,
                            onDismissRequest = { isSubjectDropdownExpanded = false }
                        ) {
                            stageSubjects.forEach { subjOption ->
                                DropdownMenuItem(
                                    text = { Text(subjOption, fontSize = 13.sp) },
                                    onClick = {
                                        viewModel.setLessonPlanSubject(subjOption)
                                        isSubjectDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Quick Subject Pills
                    if (stageSubjects.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            stageSubjects.take(6).forEach { subjChip ->
                                val isSelected = subject == subjChip
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = if (isSelected) ChipSelectedBg else Color(0xFFF1F3F4),
                                    border = BorderStroke(1.dp, if (isSelected) ChipSelectedBorder else Color.Transparent),
                                    modifier = Modifier.clickable { viewModel.setLessonPlanSubject(subjChip) }
                                ) {
                                    Text(
                                        text = subjChip,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 10.5.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) GreenPrimary else TextSecondaryLight
                                        ),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Topic Input (Mandatory)
                    Text(
                        text = "LESSON TOPIC *",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenPrimary,
                            letterSpacing = 0.5.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = topic,
                        onValueChange = { viewModel.setLessonPlanTopic(it) },
                        placeholder = { Text("e.g., Quadratic Equations, Photosynthesis, Parts of Speech") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("topic_input"),
                        shape = RoundedCornerShape(10.dp),
                        trailingIcon = {
                            if (topic.isNotBlank()) {
                                IconButton(onClick = { viewModel.setLessonPlanTopic("") }) {
                                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear Topic", tint = TextSecondaryLight)
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GreenPrimary,
                            unfocusedBorderColor = BorderCard
                        )
                    )

                    // Topic Suggestion Chips
                    if (topicSuggestions.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "💡 NERDC Suggested Topics for $subject:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.5.sp,
                                color = TextSecondaryLight,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            topicSuggestions.forEach { suggTopic ->
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = Color(0xFFF7F9F8),
                                    border = BorderStroke(1.dp, BorderSubtle),
                                    modifier = Modifier.clickable {
                                        viewModel.setLessonPlanTopic(suggTopic)
                                    }
                                ) {
                                    Text(
                                        text = suggTopic,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = TextPrimaryLight
                                        ),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Expandable Advanced Options Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showAdvancedOptions = !showAdvancedOptions }
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (showAdvancedOptions) "Hide Advanced Options" else "Show Advanced Options (Sub-topic, Term, Aids)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GreenPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        )
                        Icon(
                            imageVector = if (showAdvancedOptions) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = GreenPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Advanced Options Section
                    AnimatedVisibility(visible = showAdvancedOptions) {
                        Column(modifier = Modifier.padding(top = 8.dp)) {
                            // Sub-Topic
                            Text(
                                text = "SUB-TOPIC (OPTIONAL)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondaryLight
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = subTopic,
                                onValueChange = { viewModel.setLessonPlanSubTopic(it) },
                                placeholder = { Text("e.g., Solving by Factorization Method") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("subtopic_input"),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GreenPrimary,
                                    unfocusedBorderColor = BorderCard
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Term Selector Chips
                            Text(
                                text = "SCHOOL TERM",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondaryLight
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                SchoolTerm.values().forEach { termOpt ->
                                    val isSelected = term == termOpt
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { viewModel.setLessonPlanTerm(termOpt) },
                                        label = {
                                            Text(
                                                when (termOpt) {
                                                    SchoolTerm.FIRST_TERM -> "1st Term"
                                                    SchoolTerm.SECOND_TERM -> "2nd Term"
                                                    SchoolTerm.THIRD_TERM -> "3rd Term"
                                                },
                                                fontSize = 11.sp
                                            )
                                        },
                                        shape = RoundedCornerShape(16.dp),
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
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Lesson Duration
                            Text(
                                text = "LESSON DURATION",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondaryLight
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(
                                    "40 Minutes (Single Period)",
                                    "80 Minutes (Double Period)"
                                ).forEach { dur ->
                                    val isSelected = duration == dur
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { viewModel.setLessonPlanDuration(dur) },
                                        label = { Text(dur, fontSize = 11.sp) },
                                        shape = RoundedCornerShape(16.dp),
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
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Instructional Aids / Teaching Resources
                            Text(
                                text = "INSTRUCTIONAL MATERIALS / TEACHING AIDS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondaryLight
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = aids,
                                onValueChange = { viewModel.setLessonPlanAids(it) },
                                placeholder = { Text("e.g., Concrete realia, chalkboard illustrations, flashcards, charts") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GreenPrimary,
                                    unfocusedBorderColor = BorderCard
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Custom Pedagogical Notes
                            Text(
                                text = "SPECIAL TEACHER INSTRUCTIONS / PEDAGOGICAL EMPHASIS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondaryLight
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = customNotes,
                                onValueChange = { viewModel.setLessonPlanCustomNotes(it) },
                                placeholder = { Text("e.g., Emphasize WAEC format; include hands-on group demonstration; differentiate for slow learners") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GreenPrimary,
                                    unfocusedBorderColor = BorderCard
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Error message banner if any
                    if (errorMessage != null) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFDE8E8),
                            border = BorderStroke(1.dp, Color(0xFFF8B4B4)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
                            Text(
                                text = errorMessage ?: "",
                                color = Color(0xFF9B1C1C),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }

                    // PRIMARY GENERATE BUTTON
                    Button(
                        onClick = { viewModel.generateLessonPlanFromScreen() },
                        enabled = !isGenerating && topic.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("generate_lesson_plan_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GreenPrimary,
                            contentColor = Color.White,
                            disabledContainerColor = GreenPrimary.copy(alpha = 0.4f),
                            disabledContentColor = Color.White.copy(alpha = 0.8f)
                        ),
                        elevation = ButtonDefaults.buttonElevation(2.dp)
                    ) {
                        if (isGenerating) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Compiling with Gemini AI...",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Generate Structured Lesson Plan",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.5.sp
                            )
                        }
                    }

                    // Model and syllabus compliance label
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⚡ Powered by Gemini 3.5 Flash • 100% NERDC Compliant",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondaryLight,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }
        }

        // Active Loading Step Card
        if (isGenerating) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ChipSelectedBg),
                    border = BorderStroke(1.dp, ChipSelectedBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LinearProgressIndicator(
                            color = GreenPrimary,
                            trackColor = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = progressStep.ifBlank { "Prompting Gemini AI Educational Specialist..." },
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = GreenPrimary,
                                fontSize = 12.5.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Structuring Behavioral Objectives, Teacher Activities, and Assessment Tasks",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondaryLight,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }
        }

        // Section 2: Generated Lesson Plan Results
        activePlan?.let { plan ->
            item {
                Spacer(modifier = Modifier.height(8.dp))
                // AI Notice Pill
                if (aiNotice != null) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = BadgeEmerald,
                        border = BorderStroke(1.dp, ChipSelectedBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = GreenPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = aiNotice ?: "",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.5.sp,
                                    color = GreenPrimary
                                )
                            )
                        }
                    }
                }

                // Lesson Plan Result Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("lesson_plan_result_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, BorderCard),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Plan Title Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = plan.title,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${plan.educationLevel} • ${plan.subject} • ${plan.term}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondaryLight,
                                        fontSize = 11.5.sp
                                    )
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = ChipSelectedBg,
                                border = BorderStroke(1.dp, ChipSelectedBorder)
                            ) {
                                Text(
                                    text = "8-STEP NERDC",
                                    fontWeight = FontWeight.Bold,
                                    color = GreenPrimary,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Action Buttons Bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // View Full Document
                            OutlinedButton(
                                onClick = { onViewFullDocument(plan) },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = GreenPrimary),
                                border = BorderStroke(1.dp, ChipSelectedBorder),
                                modifier = Modifier.testTag("view_full_screen_btn")
                            ) {
                                Icon(imageVector = Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Full View", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                            }

                            // Export PDF
                            OutlinedButton(
                                onClick = { viewModel.exportAndOpenPdf(context, plan) },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = GreenPrimary),
                                border = BorderStroke(1.dp, ChipSelectedBorder),
                                modifier = Modifier.testTag("export_pdf_btn")
                            ) {
                                Icon(imageVector = Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Save PDF", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                            }

                            // Share
                            OutlinedButton(
                                onClick = { viewModel.exportAndSharePdf(context, plan) },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondaryLight),
                                border = BorderStroke(1.dp, BorderSubtle),
                                modifier = Modifier.testTag("share_lesson_plan_btn")
                            ) {
                                Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Share", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                            }

                            // Copy to clipboard
                            OutlinedButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Lesson Plan", plan.content)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Lesson Plan copied to clipboard", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondaryLight),
                                border = BorderStroke(1.dp, BorderSubtle),
                                modifier = Modifier.testTag("copy_lesson_plan_btn")
                            ) {
                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Copy", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Presentation Mode Tabs: Structured Sections vs Editable Raw
                        TabRow(
                            selectedTabIndex = resultViewMode,
                            containerColor = Color(0xFFF7F9F8),
                            indicator = { tabPositions ->
                                TabRowDefaults.SecondaryIndicator(
                                    modifier = Modifier.tabIndicatorOffset(tabPositions[resultViewMode]),
                                    color = GreenPrimary
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Tab(
                                selected = resultViewMode == 0,
                                onClick = { resultViewMode = 0 },
                                text = {
                                    Text(
                                        "Structured Sections",
                                        fontWeight = if (resultViewMode == 0) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 12.sp,
                                        color = if (resultViewMode == 0) GreenPrimary else TextSecondaryLight
                                    )
                                }
                            )
                            Tab(
                                selected = resultViewMode == 1,
                                onClick = { resultViewMode = 1 },
                                text = {
                                    Text(
                                        "Edit / Raw Text",
                                        fontWeight = if (resultViewMode == 1) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 12.sp,
                                        color = if (resultViewMode == 1) GreenPrimary else TextSecondaryLight
                                    )
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (resultViewMode == 0) {
                            // Structured 8-Step Breakdown
                            StructuredLessonPlanContent(content = plan.content)
                        } else {
                            // Editable Raw Text Mode
                            Column {
                                OutlinedTextField(
                                    value = editableContent,
                                    onValueChange = { editableContent = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(380.dp)
                                        .testTag("editable_lesson_plan_text"),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = GreenPrimary,
                                        unfocusedBorderColor = BorderCard
                                    )
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        viewModel.updateActivePlanContent(editableContent)
                                        Toast.makeText(context, "Lesson Plan updated", Toast.LENGTH_SHORT).show()
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Save Changes", fontSize = 12.sp)
                                }
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            color = BorderSubtle
                        )

                        // Gemini AI Refinement Section
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = GreenPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Refine with Gemini AI",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.5.sp
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Tap any enhancement below or type a custom instruction:",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondaryLight,
                                    fontSize = 11.5.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // Quick Refinement Prompt Pills
                            val promptSuggestions = listOf(
                                "➕ Add more local Nigerian realia examples",
                                "👥 Include interactive student group activity",
                                "❓ Add 3 more evaluation questions",
                                "🎯 Differentiate tasks for struggling learners",
                                "📐 Include chalkboard layout diagram note"
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                promptSuggestions.forEach { sugg ->
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = ChipSelectedBg,
                                        border = BorderStroke(1.dp, ChipSelectedBorder),
                                        modifier = Modifier.clickable {
                                            viewModel.refineActiveLessonPlan(sugg.substringAfter(" "))
                                        }
                                    ) {
                                        Text(
                                            text = sugg,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 10.5.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = GreenPrimary
                                            ),
                                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Custom Refinement Text Field
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = refinementPrompt,
                                    onValueChange = { refinementPrompt = it },
                                    placeholder = { Text("Ask Gemini to adjust this lesson plan...", fontSize = 12.sp) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("refinement_prompt_input"),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = GreenPrimary,
                                        unfocusedBorderColor = BorderCard
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        if (refinementPrompt.isNotBlank()) {
                                            viewModel.refineActiveLessonPlan(refinementPrompt)
                                            refinementPrompt = ""
                                        }
                                    },
                                    enabled = !isGenerating && refinementPrompt.isNotBlank(),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                                    modifier = Modifier.height(50.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = "Refine")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Parses and displays the 8 NERDC Lesson Plan steps into polished cards
 */
@Composable
fun StructuredLessonPlanContent(content: String, modifier: Modifier = Modifier) {
    val sections = remember(content) { parseLessonPlanSections(content) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        sections.forEach { section ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                border = BorderStroke(1.dp, BorderSubtle)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = section.title,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = GreenPrimary
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = section.body.trim(),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            color = TextPrimaryLight
                        )
                    )
                }
            }
        }
    }
}

data class LessonPlanSection(
    val title: String,
    val body: String
)

fun parseLessonPlanSections(content: String): List<LessonPlanSection> {
    val lines = content.lines()
    val list = mutableListOf<LessonPlanSection>()

    var currentTitle = "Overview & Context"
    val currentBody = StringBuilder()

    for (line in lines) {
        val trimmed = line.trim()
        if (trimmed.startsWith("## ") || trimmed.startsWith("### ")) {
            if (currentBody.isNotBlank()) {
                list.add(LessonPlanSection(currentTitle, currentBody.toString().trim()))
                currentBody.clear()
            }
            currentTitle = trimmed.replace(Regex("^#+\\s*"), "")
        } else {
            currentBody.appendLine(line)
        }
    }

    if (currentBody.isNotBlank()) {
        list.add(LessonPlanSection(currentTitle, currentBody.toString().trim()))
    }

    return if (list.isEmpty()) {
        listOf(LessonPlanSection("Lesson Plan", content))
    } else {
        list
    }
}
