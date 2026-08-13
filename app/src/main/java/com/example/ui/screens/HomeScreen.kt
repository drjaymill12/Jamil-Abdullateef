package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SavedMaterial
import com.example.model.MaterialCategory
import com.example.ui.components.MaterialCategoryCard
import com.example.ui.components.TeacherHeader
import com.example.ui.theme.BadgeAmber
import com.example.ui.theme.BadgeBlue
import com.example.ui.theme.BadgeCyan
import com.example.ui.theme.BadgeEmerald
import com.example.ui.theme.BadgeIndigo
import com.example.ui.theme.BadgePurple
import com.example.ui.theme.BadgeRose
import com.example.ui.theme.BadgeTeal
import com.example.ui.theme.BorderCard
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.ChipSelectedBg
import com.example.ui.theme.ChipSelectedBorder
import com.example.ui.theme.ChipSelectedText
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.GreenPrimaryDark
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.TextSecondaryLight

@Composable
fun HomeScreen(
    savedMaterials: List<SavedMaterial>,
    selectedLevelFilter: String,
    onSelectLevelFilter: (String) -> Unit,
    onOpenCategory: (MaterialCategory) -> Unit,
    onViewMaterial: (SavedMaterial) -> Unit,
    onOpenLibrary: () -> Unit,
    onExportPdf: (SavedMaterial) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_content"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Top Header Bar
        item {
            TeacherHeader(
                savedCount = savedMaterials.size,
                onOpenLibrary = onOpenLibrary
            )
        }

        // Class Level & Context Pills (Horizontally scrollable)
        item {
            ClassFilterChips(
                selectedFilter = selectedLevelFilter,
                onSelectFilter = onSelectLevelFilter,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }

        // Section Title: AI Curriculum Tools
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Curriculum Generators",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "NERDC, WAEC & BECE Aligned Classroom Tools",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            color = TextSecondaryLight
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = ChipSelectedBg,
                    border = BorderStroke(1.dp, ChipSelectedBorder)
                ) {
                    Text(
                        text = "8 AI Tools",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = ChipSelectedText,
                            fontSize = 10.sp
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // 8 Generator Cards Grid (2-column balanced layout)
        val gridCategories = listOf(
            MaterialCategory.LESSON_PLAN,
            MaterialCategory.EXAM_QUESTIONS,
            MaterialCategory.SCHEME_OF_WORK,
            MaterialCategory.LEARNING_OBJECTIVES,
            MaterialCategory.MARKING_SCHEME,
            MaterialCategory.RESULT_GENERATOR,
            MaterialCategory.QUIZ_GENERATOR,
            MaterialCategory.NOTES_GENERATOR
        )

        for (i in gridCategories.indices step 2) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val cat1 = gridCategories[i]
                    Box(modifier = Modifier.weight(1f)) {
                        MaterialCategoryCard(
                            category = cat1,
                            onClick = { onOpenCategory(cat1) }
                        )
                    }

                    if (i + 1 < gridCategories.size) {
                        val cat2 = gridCategories[i + 1]
                        Box(modifier = Modifier.weight(1f)) {
                            MaterialCategoryCard(
                                category = cat2,
                                onClick = { onOpenCategory(cat2) }
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // Full Width Nigerian Curriculum Banner Card
        item {
            NigerianCurriculumCard(
                onClick = { onOpenCategory(MaterialCategory.CURRICULUM_GUIDE) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }

        // Recent Saved Teaching Materials
        if (savedMaterials.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Teaching Materials",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "View All",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = GreenPrimary,
                            fontSize = 12.sp
                        ),
                        modifier = Modifier
                            .clickable { onOpenLibrary() }
                            .padding(4.dp)
                    )
                }
            }

            items(savedMaterials.take(4)) { item ->
                RecentMaterialItem(
                    material = item,
                    onClick = { onViewMaterial(item) },
                    onExportPdf = { onExportPdf(item) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun NigerianCurriculumCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("nigerian_curriculum_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = GreenPrimary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "🇳🇬",
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Nigerian Curriculum",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    )
                    Text(
                        text = "National Standard Guidelines & NERDC Syllabus",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Open Curriculum",
                tint = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun ClassFilterChips(
    selectedFilter: String,
    onSelectFilter: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val filters = listOf("Grade: JSS3", "Term: 1st Term", "Subject: Mathematics", "Kindergarten", "Primary (1-6)", "Senior Sec (SSS)")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Active preset chip: Grade JSS3
        val isFirstSelected = selectedFilter == "Junior Sec (JSS)" || selectedFilter == "Grade: JSS3"
        FilterChip(
            selected = isFirstSelected,
            onClick = { onSelectFilter("Junior Sec (JSS)") },
            label = {
                Text(
                    text = "Grade: JSS3",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
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
                selected = isFirstSelected,
                selectedBorderColor = ChipSelectedBorder,
                borderColor = BorderSubtle
            )
        )

        // Term chip
        FilterChip(
            selected = false,
            onClick = { /* Informational/filter */ },
            label = {
                Text(
                    text = "Term: 1st Term",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            },
            shape = RoundedCornerShape(20.dp),
            colors = FilterChipDefaults.filterChipColors(
                containerColor = Color.White,
                labelColor = TextSecondaryLight
            ),
            border = FilterChipDefaults.filterChipBorder(
                enabled = true,
                selected = false,
                borderColor = BorderSubtle
            )
        )

        // Subject chip
        FilterChip(
            selected = false,
            onClick = { /* Informational/filter */ },
            label = {
                Text(
                    text = "Subject: Mathematics",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            },
            shape = RoundedCornerShape(20.dp),
            colors = FilterChipDefaults.filterChipColors(
                containerColor = Color.White,
                labelColor = TextSecondaryLight
            ),
            border = FilterChipDefaults.filterChipBorder(
                enabled = true,
                selected = false,
                borderColor = BorderSubtle
            )
        )

        // Other level options
        listOf("Primary (1-6)", "Senior Sec (SSS)", "Kindergarten", "All").forEach { filter ->
            val isSelected = selectedFilter == filter
            FilterChip(
                selected = isSelected,
                onClick = { onSelectFilter(filter) },
                label = {
                    Text(
                        text = filter,
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

@Composable
fun RecentMaterialItem(
    material: SavedMaterial,
    onClick: () -> Unit,
    onExportPdf: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (badgeBg, emoji) = when (material.category) {
        "LESSON_PLAN" -> Pair(BadgeBlue, "📚")
        "EXAM_QUESTIONS" -> Pair(BadgePurple, "📝")
        "SCHEME_OF_WORK" -> Pair(BadgeTeal, "📖")
        "LEARNING_OBJECTIVES" -> Pair(BadgeAmber, "🎯")
        "MARKING_SCHEME" -> Pair(BadgeEmerald, "✅")
        "RESULT_GENERATOR" -> Pair(BadgeIndigo, "📊")
        "QUIZ_GENERATOR" -> Pair(BadgeRose, "🧠")
        "NOTES_GENERATOR" -> Pair(BadgeCyan, "📄")
        else -> Pair(ChipSelectedBg, "🇳🇬")
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("recent_item_${material.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, BorderCard)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(badgeBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = emoji, fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = material.title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.5.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${material.subject} • ${material.educationLevel}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            color = TextSecondaryLight
                        ),
                        maxLines = 1
                    )
                }
            }

            // PDF Download Icon Button
            IconButton(
                onClick = onExportPdf,
                modifier = Modifier.testTag("download_pdf_btn_${material.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Download PDF",
                    tint = GreenPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

