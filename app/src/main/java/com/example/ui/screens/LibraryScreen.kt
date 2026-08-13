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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SavedMaterial
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GreenPrimary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LibraryScreen(
    savedMaterials: List<SavedMaterial>,
    onViewMaterial: (SavedMaterial) -> Unit,
    onExportPdf: (SavedMaterial) -> Unit,
    onToggleStarred: (SavedMaterial) -> Unit,
    onDelete: (SavedMaterial) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryFilter by remember { mutableStateOf("ALL") }
    var showOnlyStarred by remember { mutableStateOf(false) }

    val filteredMaterials = savedMaterials.filter { item ->
        val matchesQuery = searchQuery.isBlank() ||
                item.title.contains(searchQuery, ignoreCase = true) ||
                item.subject.contains(searchQuery, ignoreCase = true) ||
                item.topic.contains(searchQuery, ignoreCase = true) ||
                item.educationLevel.contains(searchQuery, ignoreCase = true)

        val matchesCategory = selectedCategoryFilter == "ALL" || item.category == selectedCategoryFilter
        val matchesStarred = !showOnlyStarred || item.isStarred

        matchesQuery && matchesCategory && matchesStarred
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("library_screen_root")
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search by topic, subject, or class...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .testTag("library_search_field"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenPrimary
            ),
            singleLine = true
        )

        // Filter Chips Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // All
            FilterChip(
                selected = selectedCategoryFilter == "ALL" && !showOnlyStarred,
                onClick = {
                    selectedCategoryFilter = "ALL"
                    showOnlyStarred = false
                },
                label = { Text("All (${savedMaterials.size})") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = GreenPrimary,
                    selectedLabelColor = Color.White
                )
            )

            // Starred Only
            FilterChip(
                selected = showOnlyStarred,
                onClick = { showOnlyStarred = !showOnlyStarred },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if (showOnlyStarred) GoldAccent else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                },
                label = { Text("Starred") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = GreenPrimary,
                    selectedLabelColor = Color.White
                )
            )

            val categoryOptions = listOf(
                "LESSON_PLAN" to "📚 Lesson Plans",
                "EXAM_QUESTIONS" to "📝 Exams",
                "SCHEME_OF_WORK" to "📖 Schemes",
                "LEARNING_OBJECTIVES" to "🎯 Objectives",
                "MARKING_SCHEME" to "✅ Marking",
                "RESULT_GENERATOR" to "📊 Results",
                "QUIZ_GENERATOR" to "🧠 Quizzes",
                "NOTES_GENERATOR" to "📄 Notes"
            )

            for ((key, label) in categoryOptions) {
                val isSelected = selectedCategoryFilter == key
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        selectedCategoryFilter = if (isSelected) "ALL" else key
                    },
                    label = { Text(label) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GreenPrimary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Material List
        if (filteredMaterials.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (savedMaterials.isEmpty()) "No saved materials yet" else "No matching materials found",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Generate lesson plans, exams, or notes to save them for offline classroom use.",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredMaterials, key = { it.id }) { material ->
                    LibraryMaterialCard(
                        material = material,
                        onClick = { onViewMaterial(material) },
                        onExportPdf = { onExportPdf(material) },
                        onToggleStarred = { onToggleStarred(material) },
                        onDelete = { onDelete(material) }
                    )
                }
            }
        }
    }
}

@Composable
fun LibraryMaterialCard(
    material: SavedMaterial,
    onClick: () -> Unit,
    onExportPdf: () -> Unit,
    onToggleStarred: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val emoji = when (material.category) {
        "LESSON_PLAN" -> "📚"
        "EXAM_QUESTIONS" -> "📝"
        "SCHEME_OF_WORK" -> "📖"
        "LEARNING_OBJECTIVES" -> "🎯"
        "MARKING_SCHEME" -> "✅"
        "RESULT_GENERATOR" -> "📊"
        "QUIZ_GENERATOR" -> "🧠"
        "NOTES_GENERATOR" -> "📄"
        else -> "🇳🇬"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("library_card_${material.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
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
                                fontSize = 14.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(material.createdAt))
                        Text(
                            text = "${material.subject} • ${material.educationLevel} • $dateStr",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            maxLines = 1
                        )
                    }
                }

                // Star Button
                IconButton(
                    onClick = onToggleStarred,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (material.isStarred) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = "Star",
                        tint = if (material.isStarred) GoldAccent else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Row: Download PDF & Delete
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = material.term,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenPrimary
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onExportPdf,
                        modifier = Modifier.testTag("lib_download_pdf_${material.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Download PDF",
                            tint = GreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.testTag("lib_delete_${material.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
