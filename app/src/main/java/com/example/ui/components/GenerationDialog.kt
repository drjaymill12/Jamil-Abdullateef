package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.EducationLevel
import com.example.model.MaterialCategory
import com.example.model.NigerianCurriculumData
import com.example.model.SchoolTerm
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GreenPrimary

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun GenerationDialog(
    category: MaterialCategory,
    initialLevel: EducationLevel = EducationLevel.JSS_2,
    isGenerating: Boolean,
    progressMessage: String,
    onDismiss: () -> Unit,
    onGenerate: (
        category: MaterialCategory,
        level: EducationLevel,
        subject: String,
        term: SchoolTerm,
        topic: String,
        subTopic: String,
        customInstructions: String
    ) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedLevel by remember { mutableStateOf(initialLevel) }
    var selectedTerm by remember { mutableStateOf(SchoolTerm.FIRST_TERM) }

    val defaultSubject = when (selectedLevel.category) {
        "Kindergarten" -> "Numeracy / Early Mathematics"
        "Primary" -> "Basic Science and Technology"
        "Junior Secondary" -> "Basic Science"
        else -> "Mathematics"
    }

    var selectedSubject by remember(selectedLevel) {
        val subjects = NigerianCurriculumData.SUBJECTS_BY_LEVEL[selectedLevel.category] ?: listOf("Mathematics")
        mutableStateOf(subjects.firstOrNull() ?: defaultSubject)
    }

    var topicInput by remember { mutableStateOf("") }
    var subTopicInput by remember { mutableStateOf("") }
    var customInstructionsInput by remember { mutableStateOf("") }

    var subjectDropdownExpanded by remember { mutableStateOf(false) }
    var levelDropdownExpanded by remember { mutableStateOf(false) }

    // Preloaded topic suggestions based on selected subject
    val availableTopics = NigerianCurriculumData.TOPIC_SUGGESTIONS[selectedSubject]
        ?: listOf("Introduction to $selectedSubject", "Fundamental Principles", "Practical Applications", "Revision Exercises")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
                .testTag("generation_sheet_${category.name.lowercase()}")
        ) {
            // Header with Category Title & Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = category.iconEmoji, fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Generate ${category.title}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Nigerian Educational Curriculum (NERDC / WAEC)",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    enabled = !isGenerating,
                    modifier = Modifier.testTag("close_generation_dialog")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 1. CLASS / LEVEL SELECTOR (KG 1 to SS 3)
            Text(
                text = "1. Class / School Level (Kindergarten to SS 3)",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))

            // Quick Category Filter Tabs (KG, Primary, JSS, SSS)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val categories = listOf("Kindergarten", "Primary", "Junior Secondary", "Senior Secondary")
                for (cat in categories) {
                    val isCatSelected = selectedLevel.category == cat
                    FilterChip(
                        selected = isCatSelected,
                        onClick = {
                            val firstLevelInCat = EducationLevel.values().firstOrNull { it.category == cat }
                            if (firstLevelInCat != null) {
                                selectedLevel = firstLevelInCat
                            }
                        },
                        label = {
                            Text(
                                text = when (cat) {
                                    "Kindergarten" -> "Early Years / KG"
                                    "Primary" -> "Primary (1-6)"
                                    "Junior Secondary" -> "JSS (1-3)"
                                    else -> "SSS (1-3)"
                                },
                                fontSize = 12.sp,
                                fontWeight = if (isCatSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GreenPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Specific Class Dropdown
            ExposedDropdownMenuBox(
                expanded = levelDropdownExpanded,
                onExpandedChange = { levelDropdownExpanded = !levelDropdownExpanded && !isGenerating }
            ) {
                OutlinedTextField(
                    value = "${selectedLevel.displayName} (${selectedLevel.ageGroup})",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Specific Class") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = levelDropdownExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .testTag("class_selector_dropdown"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary
                    )
                )

                ExposedDropdownMenu(
                    expanded = levelDropdownExpanded,
                    onDismissRequest = { levelDropdownExpanded = false }
                ) {
                    EducationLevel.values().forEach { level ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "${level.displayName} • ${level.ageGroup}",
                                    fontWeight = if (level == selectedLevel) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            onClick = {
                                selectedLevel = level
                                levelDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. TERM SELECTOR
            Text(
                text = "2. Academic Term",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SchoolTerm.values().forEach { term ->
                    val isSelected = selectedTerm == term
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedTerm = term },
                        label = {
                            Text(
                                text = when (term) {
                                    SchoolTerm.FIRST_TERM -> "1st Term"
                                    SchoolTerm.SECOND_TERM -> "2nd Term"
                                    SchoolTerm.THIRD_TERM -> "3rd Term"
                                },
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        modifier = Modifier.weight(1f),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GreenPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. SUBJECT SELECTOR
            Text(
                text = "3. Subject (Nigerian Curriculum)",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))

            val currentSubjects = NigerianCurriculumData.SUBJECTS_BY_LEVEL[selectedLevel.category]
                ?: listOf("Mathematics", "English Language", "Basic Science")

            ExposedDropdownMenuBox(
                expanded = subjectDropdownExpanded,
                onExpandedChange = { subjectDropdownExpanded = !subjectDropdownExpanded && !isGenerating }
            ) {
                OutlinedTextField(
                    value = selectedSubject,
                    onValueChange = { selectedSubject = it },
                    label = { Text("Select or Type Subject") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = subjectDropdownExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .testTag("subject_input_field"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary
                    )
                )

                ExposedDropdownMenu(
                    expanded = subjectDropdownExpanded,
                    onDismissRequest = { subjectDropdownExpanded = false }
                ) {
                    currentSubjects.forEach { subj ->
                        DropdownMenuItem(
                            text = { Text(subj) },
                            onClick = {
                                selectedSubject = subj
                                subjectDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4. TOPIC INPUT & SUGGESTION CHIPS
            Text(
                text = "4. Topic & Sub-Topic Details",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = topicInput,
                onValueChange = { topicInput = it },
                label = { Text("Main Topic *") },
                placeholder = { Text(category.samplePromptPlaceholder) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("topic_input_field"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary
                ),
                singleLine = true
            )

            // Topic Suggestion Chips
            if (availableTopics.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "NERDC Syllabus Suggestions:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    availableTopics.take(6).forEach { suggestion ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.clickable {
                                topicInput = suggestion
                            }
                        ) {
                            Text(
                                text = suggestion,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = subTopicInput,
                onValueChange = { subTopicInput = it },
                label = { Text("Sub-Topic (Optional)") },
                placeholder = { Text("e.g., Types, Formulas, Calculations, Experiments") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("subtopic_input_field"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = customInstructionsInput,
                onValueChange = { customInstructionsInput = it },
                label = { Text("Special Teacher Instructions (Optional)") },
                placeholder = { Text("e.g., Include WAEC 2024 past question format, 80-min double period, practical lab emphasis") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("custom_instructions_field"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary
                ),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(20.dp))

            // GENERATE BUTTON OR PROGRESS
            if (isGenerating) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = GreenPrimary,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.5.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = progressMessage.ifBlank { "Compiling curriculum-aligned content..." },
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp
                            ),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            } else {
                Button(
                    onClick = {
                        val topicToUse = topicInput.ifBlank {
                            availableTopics.firstOrNull() ?: "Foundational Principles of $selectedSubject"
                        }
                        onGenerate(
                            category,
                            selectedLevel,
                            selectedSubject,
                            selectedTerm,
                            topicToUse,
                            subTopicInput,
                            customInstructionsInput
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_generate_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GreenPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Generate ${category.title} (NERDC 🇳🇬)",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
