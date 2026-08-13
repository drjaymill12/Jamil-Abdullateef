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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SavedMaterial
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.GreenPrimaryDark
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentViewerScreen(
    material: SavedMaterial,
    onBack: () -> Unit,
    onDownloadPdf: (SavedMaterial) -> Unit,
    onSharePdf: (SavedMaterial) -> Unit,
    onToggleStarred: (SavedMaterial) -> Unit,
    onDelete: (SavedMaterial) -> Unit,
    onSaveContent: (SavedMaterial, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var isEditMode by remember { mutableStateOf(false) }
    var editedContent by remember(material) { mutableStateOf(material.content) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("document_viewer_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = material.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                            maxLines = 1
                        )
                        Text(
                            text = "${material.subject} • ${material.educationLevel} • ${material.term}",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("viewer_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Star toggle
                    IconButton(
                        onClick = { onToggleStarred(material) },
                        modifier = Modifier.testTag("viewer_star_btn")
                    ) {
                        Icon(
                            imageVector = if (material.isStarred) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Star",
                            tint = if (material.isStarred) GoldAccent else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Edit / Done toggle
                    IconButton(
                        onClick = {
                            if (isEditMode) {
                                onSaveContent(material, editedContent)
                                Toast.makeText(context, "Changes saved!", Toast.LENGTH_SHORT).show()
                            }
                            isEditMode = !isEditMode
                        },
                        modifier = Modifier.testTag("viewer_edit_btn")
                    ) {
                        Icon(
                            imageVector = if (isEditMode) Icons.Default.Check else Icons.Default.Edit,
                            contentDescription = if (isEditMode) "Save" else "Edit",
                            tint = if (isEditMode) GreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Delete button
                    IconButton(
                        onClick = {
                            onDelete(material)
                            onBack()
                        },
                        modifier = Modifier.testTag("viewer_delete_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            // PDF & Action Toolbar
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Download PDF button
                    Button(
                        onClick = { onDownloadPdf(material) },
                        modifier = Modifier
                            .weight(1.2f)
                            .height(46.dp)
                            .testTag("viewer_download_pdf_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GreenPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Download PDF",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    // Share PDF button
                    FilledTonalButton(
                        onClick = { onSharePdf(material) },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("viewer_share_pdf_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Share PDF", fontSize = 12.sp)
                    }

                    // Copy Text button
                    OutlinedButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(material.content))
                            Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .height(46.dp)
                            .testTag("viewer_copy_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Text",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
        ) {
            // Document Header Metadata Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                    border = BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.2f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🇳🇬 Nigerian National Curriculum (NERDC)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GreenPrimary
                                )
                            )
                            val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(material.createdAt))
                            Text(
                                text = dateStr,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("SUBJECT", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, color = GreenPrimaryDark))
                                Text(material.subject, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("CLASS / LEVEL", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, color = GreenPrimaryDark))
                                Text(material.educationLevel, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("TERM", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, color = GreenPrimaryDark))
                                Text(material.term, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Editable or Rendered Content
            item {
                if (isEditMode) {
                    OutlinedTextField(
                        value = editedContent,
                        onValueChange = { editedContent = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("viewer_edit_content_field"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GreenPrimary
                        ),
                        label = { Text("Edit Document Content") }
                    )
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Render line-by-line formatted content
                            val lines = material.content.split("\n")
                            for (line in lines) {
                                val trimmed = line.trimEnd()
                                when {
                                    trimmed.startsWith("# ") -> {
                                        Text(
                                            text = trimmed.removePrefix("# "),
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = GreenPrimary,
                                                fontSize = 20.sp
                                            ),
                                            modifier = Modifier.padding(top = 10.dp, bottom = 6.dp)
                                        )
                                    }
                                    trimmed.startsWith("## ") -> {
                                        Text(
                                            text = trimmed.removePrefix("## "),
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = GreenPrimaryDark,
                                                fontSize = 16.sp
                                            ),
                                            modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                                        )
                                    }
                                    trimmed.startsWith("### ") -> {
                                        Text(
                                            text = trimmed.removePrefix("### "),
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.primary,
                                                fontSize = 14.sp
                                            ),
                                            modifier = Modifier.padding(top = 8.dp, bottom = 2.dp)
                                        )
                                    }
                                    trimmed.startsWith("---") || trimmed.startsWith("***") -> {
                                        Surface(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(1.dp)
                                                .padding(vertical = 8.dp),
                                            color = MaterialTheme.colorScheme.outlineVariant
                                        ) {}
                                    }
                                    trimmed.startsWith("- ") || trimmed.startsWith("* ") || trimmed.startsWith("• ") -> {
                                        Row(
                                            modifier = Modifier.padding(vertical = 2.dp),
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Text("• ", color = GreenPrimary, fontWeight = FontWeight.Bold)
                                            Text(
                                                text = trimmed.removePrefix("- ").removePrefix("* ").removePrefix("• "),
                                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp)
                                            )
                                        }
                                    }
                                    trimmed.isBlank() -> {
                                        Spacer(modifier = Modifier.height(6.dp))
                                    }
                                    else -> {
                                        Text(
                                            text = trimmed,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                lineHeight = 21.sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            ),
                                            modifier = Modifier.padding(vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
