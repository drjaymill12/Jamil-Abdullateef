package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.EducationLevel
import com.example.model.MaterialCategory
import com.example.ui.components.GenerationDialog
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.ChipSelectedBg
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.TextSecondaryLight
import com.example.viewmodel.AppNavTab
import com.example.viewmodel.TeacherMateViewModel

@Composable
fun MainAppContent(
    viewModel: TeacherMateViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentTab by viewModel.currentNavTab.collectAsState()
    val savedMaterials by viewModel.savedMaterials.collectAsState()
    val selectedLevelFilter by viewModel.selectedLevelFilter.collectAsState()
    val currentViewingMaterial by viewModel.currentViewingMaterial.collectAsState()
    val selectedCategoryForDialog by viewModel.selectedCategoryForDialog.collectAsState()
    val generationState by viewModel.generationState.collectAsState()

    // If viewing a document, show full-screen DocumentViewerScreen
    val activeMaterial = currentViewingMaterial
    if (activeMaterial != null) {
        DocumentViewerScreen(
            material = activeMaterial,
            onBack = { viewModel.viewMaterial(null) },
            onDownloadPdf = { mat -> viewModel.exportAndOpenPdf(context, mat) },
            onSharePdf = { mat -> viewModel.exportAndSharePdf(context, mat) },
            onToggleStarred = { mat -> viewModel.toggleStarred(mat) },
            onDelete = { mat -> viewModel.deleteMaterial(mat) },
            onSaveContent = { mat, newContent -> viewModel.updateMaterialContent(mat, newContent) }
        )
        return
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("main_app_scaffold"),
        bottomBar = {
            Surface(
                border = BorderStroke(1.dp, BorderSubtle),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp
            ) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp
                ) {
                    NavigationBarItem(
                        selected = currentTab == AppNavTab.HOME,
                        onClick = { viewModel.setNavTab(AppNavTab.HOME) },
                        icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home", fontSize = 10.5.sp, fontWeight = if (currentTab == AppNavTab.HOME) FontWeight.Bold else FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GreenPrimary,
                            selectedTextColor = GreenPrimary,
                            unselectedIconColor = TextSecondaryLight,
                            unselectedTextColor = TextSecondaryLight,
                            indicatorColor = ChipSelectedBg
                        ),
                        modifier = Modifier.testTag("nav_item_home")
                    )

                    NavigationBarItem(
                        selected = currentTab == AppNavTab.LIBRARY,
                        onClick = { viewModel.setNavTab(AppNavTab.LIBRARY) },
                        icon = { Icon(imageVector = Icons.Default.Bookmark, contentDescription = "Library") },
                        label = { Text("Downloads (${savedMaterials.size})", fontSize = 10.5.sp, fontWeight = if (currentTab == AppNavTab.LIBRARY) FontWeight.Bold else FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GreenPrimary,
                            selectedTextColor = GreenPrimary,
                            unselectedIconColor = TextSecondaryLight,
                            unselectedTextColor = TextSecondaryLight,
                            indicatorColor = ChipSelectedBg
                        ),
                        modifier = Modifier.testTag("nav_item_library")
                    )

                    NavigationBarItem(
                        selected = currentTab == AppNavTab.CURRICULUM,
                        onClick = { viewModel.setNavTab(AppNavTab.CURRICULUM) },
                        icon = { Icon(imageVector = Icons.Default.AutoStories, contentDescription = "Curriculum") },
                        label = { Text("Curriculum", fontSize = 10.5.sp, fontWeight = if (currentTab == AppNavTab.CURRICULUM) FontWeight.Bold else FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GreenPrimary,
                            selectedTextColor = GreenPrimary,
                            unselectedIconColor = TextSecondaryLight,
                            unselectedTextColor = TextSecondaryLight,
                            indicatorColor = ChipSelectedBg
                        ),
                        modifier = Modifier.testTag("nav_item_curriculum")
                    )

                    NavigationBarItem(
                        selected = currentTab == AppNavTab.RESULT_TOOL,
                        onClick = { viewModel.setNavTab(AppNavTab.RESULT_TOOL) },
                        icon = { Icon(imageVector = Icons.Default.Assessment, contentDescription = "Remarks") },
                        label = { Text("Results", fontSize = 10.5.sp, fontWeight = if (currentTab == AppNavTab.RESULT_TOOL) FontWeight.Bold else FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GreenPrimary,
                            selectedTextColor = GreenPrimary,
                            unselectedIconColor = TextSecondaryLight,
                            unselectedTextColor = TextSecondaryLight,
                            indicatorColor = ChipSelectedBg
                        ),
                        modifier = Modifier.testTag("nav_item_result_tool")
                    )
                }
            }
        }
    ) { innerPadding ->
        when (currentTab) {
            AppNavTab.HOME -> {
                HomeScreen(
                    savedMaterials = savedMaterials,
                    selectedLevelFilter = selectedLevelFilter,
                    onSelectLevelFilter = { viewModel.setLevelFilter(it) },
                    onOpenCategory = { cat ->
                        if (cat == MaterialCategory.CURRICULUM_GUIDE) {
                            viewModel.setNavTab(AppNavTab.CURRICULUM)
                        } else if (cat == MaterialCategory.RESULT_GENERATOR) {
                            viewModel.setNavTab(AppNavTab.RESULT_TOOL)
                        } else {
                            viewModel.openGeneratorDialog(cat)
                        }
                    },
                    onViewMaterial = { mat -> viewModel.viewMaterial(mat) },
                    onOpenLibrary = { viewModel.setNavTab(AppNavTab.LIBRARY) },
                    onExportPdf = { mat -> viewModel.exportAndOpenPdf(context, mat) },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            AppNavTab.LIBRARY -> {
                LibraryScreen(
                    savedMaterials = savedMaterials,
                    onViewMaterial = { mat -> viewModel.viewMaterial(mat) },
                    onExportPdf = { mat -> viewModel.exportAndOpenPdf(context, mat) },
                    onToggleStarred = { mat -> viewModel.toggleStarred(mat) },
                    onDelete = { mat -> viewModel.deleteMaterial(mat) },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            AppNavTab.CURRICULUM -> {
                CurriculumHubScreen(
                    onGenerateForTopic = { cat, level, subj, topic ->
                        viewModel.generateMaterial(
                            category = cat,
                            schoolLevel = level,
                            subject = subj,
                            term = com.example.model.SchoolTerm.FIRST_TERM,
                            topic = topic,
                            onCompleted = { mat -> viewModel.viewMaterial(mat) }
                        )
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            AppNavTab.RESULT_TOOL -> {
                ResultGeneratorScreen(
                    onSaveMaterial = { mat ->
                        viewModel.generateMaterial(
                            category = MaterialCategory.RESULT_GENERATOR,
                            schoolLevel = EducationLevel.JSS_2,
                            subject = mat.subject,
                            term = com.example.model.SchoolTerm.FIRST_TERM,
                            topic = mat.topic,
                            onCompleted = { saved -> viewModel.viewMaterial(saved) }
                        )
                    },
                    onExportPdf = { mat -> viewModel.exportAndOpenPdf(context, mat) },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }

    // Modal Sheet for Generation Dialog
    val categoryToGenerate = selectedCategoryForDialog
    if (categoryToGenerate != null) {
        val initialLevel = when (selectedLevelFilter) {
            "Kindergarten" -> EducationLevel.KG_1
            "Primary (1-6)" -> EducationLevel.PRIMARY_4
            "Senior Sec (SSS)" -> EducationLevel.SS_2
            else -> EducationLevel.JSS_2
        }

        GenerationDialog(
            category = categoryToGenerate,
            initialLevel = initialLevel,
            isGenerating = generationState.isGenerating,
            progressMessage = generationState.progressMessage,
            onDismiss = { viewModel.closeGeneratorDialog() },
            onGenerate = { cat, level, subject, term, topic, subTopic, customInstructions ->
                viewModel.generateMaterial(
                    category = cat,
                    schoolLevel = level,
                    subject = subject,
                    term = term,
                    topic = topic,
                    subTopic = subTopic,
                    customInstructions = customInstructions,
                    onCompleted = { created ->
                        viewModel.viewMaterial(created)
                    }
                )
            }
        )
    }
}
