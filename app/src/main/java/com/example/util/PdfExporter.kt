package com.example.util

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.print.PrintAttributes
import android.print.PrintManager
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.data.SavedMaterial
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfExporter {

    // A4 dimensions in PostScript points (72 DPI)
    private const val PAGE_WIDTH = 595
    private const val PAGE_HEIGHT = 842
    private const val MARGIN_X = 40f
    private const val MARGIN_TOP = 40f
    private const val MARGIN_BOTTOM = 45f
    private const val CONTENT_WIDTH = PAGE_WIDTH - (MARGIN_X * 2)

    fun exportToPdfFile(context: Context, material: SavedMaterial): File? {
        val pdfDocument = PdfDocument()

        val textPaint = Paint().apply {
            color = Color.rgb(20, 35, 28)
            textSize = 10.5f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        }

        val boldPaint = Paint().apply {
            color = Color.rgb(0, 75, 50)
            textSize = 11.5f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        }

        val h1Paint = Paint().apply {
            color = Color.rgb(0, 106, 78)
            textSize = 16f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        }

        val h2Paint = Paint().apply {
            color = Color.rgb(0, 120, 85)
            textSize = 13f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        }

        val metaLabelPaint = Paint().apply {
            color = Color.rgb(90, 110, 100)
            textSize = 9.5f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        }

        val metaValPaint = Paint().apply {
            color = Color.rgb(15, 30, 22)
            textSize = 10f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        }

        val bgPaint = Paint().apply {
            isAntiAlias = true
        }

        // Prepare line-by-line items to paginate
        val lines = material.content.split("\n")
        val pagesToDraw = mutableListOf<List<PdfLineItem>>()
        var currentPageLines = mutableListOf<PdfLineItem>()
        var currentY = MARGIN_TOP + 120f // Room for header on page 1

        for (rawLine in lines) {
            val trimmed = rawLine.trimEnd()
            val itemType = when {
                trimmed.startsWith("# ") -> LineType.H1
                trimmed.startsWith("## ") -> LineType.H2
                trimmed.startsWith("### ") -> LineType.H3
                trimmed.startsWith("---") || trimmed.startsWith("***") -> LineType.DIVIDER
                trimmed.startsWith("- ") || trimmed.startsWith("* ") || trimmed.startsWith("• ") -> LineType.BULLET
                trimmed.matches(Regex("^\\d+\\..*")) -> LineType.NUMBERED
                trimmed.isBlank() -> LineType.EMPTY
                else -> LineType.BODY
            }

            val text = when (itemType) {
                LineType.H1 -> trimmed.removePrefix("# ").trim()
                LineType.H2 -> trimmed.removePrefix("## ").trim()
                LineType.H3 -> trimmed.removePrefix("### ").trim()
                LineType.BULLET -> trimmed.removePrefix("- ").removePrefix("* ").removePrefix("• ").trim()
                LineType.DIVIDER -> ""
                else -> trimmed
            }

            val itemPaint = when (itemType) {
                LineType.H1 -> h1Paint
                LineType.H2, LineType.H3 -> h2Paint
                else -> if (text.startsWith("**") && text.endsWith("**")) boldPaint else textPaint
            }

            val lineHeight = when (itemType) {
                LineType.H1 -> 24f
                LineType.H2 -> 20f
                LineType.H3 -> 18f
                LineType.EMPTY -> 10f
                LineType.DIVIDER -> 12f
                else -> 15f
            }

            // Word wrap
            val wrappedLines = wrapText(text, itemPaint, if (itemType == LineType.BULLET || itemType == LineType.NUMBERED) CONTENT_WIDTH - 20f else CONTENT_WIDTH)
            val totalHeightForBlock = if (wrappedLines.isEmpty()) lineHeight else wrappedLines.size * lineHeight

            if (currentY + totalHeightForBlock > PAGE_HEIGHT - MARGIN_BOTTOM) {
                // New page needed
                pagesToDraw.add(currentPageLines)
                currentPageLines = mutableListOf()
                currentY = MARGIN_TOP + 40f // Subsequent page header
            }

            if (wrappedLines.isEmpty()) {
                currentPageLines.add(PdfLineItem(itemType, "", itemPaint, lineHeight))
                currentY += lineHeight
            } else {
                for ((idx, wLine) in wrappedLines.withIndex()) {
                    val lineTypeToUse = if (idx == 0) itemType else LineType.BODY
                    currentPageLines.add(PdfLineItem(lineTypeToUse, wLine, itemPaint, lineHeight))
                    currentY += lineHeight
                }
            }
        }

        if (currentPageLines.isNotEmpty()) {
            pagesToDraw.add(currentPageLines)
        }

        val totalPages = pagesToDraw.size.coerceAtLeast(1)

        // Draw each page
        for (pageIdx in 0 until totalPages) {
            val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageIdx + 1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            // Page Background
            bgPaint.color = Color.WHITE
            canvas.drawRect(0f, 0f, PAGE_WIDTH.toFloat(), PAGE_HEIGHT.toFloat(), bgPaint)

            var yPos = MARGIN_TOP

            // Header Banner
            if (pageIdx == 0) {
                // Top emerald strip
                bgPaint.color = Color.rgb(0, 106, 78) // Nigerian Emerald
                canvas.drawRect(0f, 0f, PAGE_WIDTH.toFloat(), 34f, bgPaint)

                // Gold decorative accent line
                bgPaint.color = Color.rgb(217, 119, 6)
                canvas.drawRect(0f, 34f, PAGE_WIDTH.toFloat(), 37f, bgPaint)

                val headerTitlePaint = Paint().apply {
                    color = Color.WHITE
                    textSize = 12f
                    isAntiAlias = true
                    typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                }
                canvas.drawText("TEACHERMATE NG • NIGERIAN CURRICULUM TEACHING RESOURCE", MARGIN_X, 22f, headerTitlePaint)

                val nerdcBadgePaint = Paint().apply {
                    color = Color.rgb(230, 245, 235)
                    textSize = 9f
                    isAntiAlias = true
                    typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                }
                canvas.drawText("NERDC / WAEC / NECO ALIGNED", PAGE_WIDTH - MARGIN_X - 160f, 22f, nerdcBadgePaint)

                yPos = 52f

                // Metadata Card Box
                bgPaint.color = Color.rgb(243, 248, 245)
                val metaRect = RectF(MARGIN_X, yPos, PAGE_WIDTH - MARGIN_X, yPos + 60f)
                canvas.drawRoundRect(metaRect, 6f, 6f, bgPaint)

                // Border around metadata box
                val borderPaint = Paint().apply {
                    color = Color.rgb(200, 225, 215)
                    style = Paint.Style.STROKE
                    strokeWidth = 1f
                    isAntiAlias = true
                }
                canvas.drawRoundRect(metaRect, 6f, 6f, borderPaint)

                val col1X = MARGIN_X + 12f
                val col2X = MARGIN_X + (CONTENT_WIDTH / 2f) + 10f

                canvas.drawText("SUBJECT:", col1X, yPos + 18f, metaLabelPaint)
                canvas.drawText(material.subject, col1X + 55f, yPos + 18f, metaValPaint)

                canvas.drawText("CLASS / LEVEL:", col2X, yPos + 18f, metaLabelPaint)
                canvas.drawText(material.educationLevel, col2X + 85f, yPos + 18f, metaValPaint)

                canvas.drawText("TERM:", col1X, yPos + 36f, metaLabelPaint)
                canvas.drawText(material.term, col1X + 55f, yPos + 36f, metaValPaint)

                canvas.drawText("RESOURCE TYPE:", col2X, yPos + 36f, metaLabelPaint)
                canvas.drawText(material.category.replace("_", " "), col2X + 85f, yPos + 36f, metaValPaint)

                val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(material.createdAt))
                canvas.drawText("TOPIC:", col1X, yPos + 52f, metaLabelPaint)
                val cleanTopic = if (material.topic.length > 35) material.topic.take(35) + "..." else material.topic
                canvas.drawText(cleanTopic, col1X + 55f, yPos + 52f, metaValPaint)

                canvas.drawText("DATE:", col2X, yPos + 52f, metaLabelPaint)
                canvas.drawText(dateStr, col2X + 85f, yPos + 52f, metaValPaint)

                yPos += 75f
            } else {
                // Subsequent page mini header
                bgPaint.color = Color.rgb(0, 106, 78)
                canvas.drawRect(MARGIN_X, 20f, PAGE_WIDTH - MARGIN_X, 22f, bgPaint)

                val miniHeaderPaint = Paint().apply {
                    color = Color.rgb(100, 125, 115)
                    textSize = 8.5f
                    isAntiAlias = true
                }
                canvas.drawText("${material.subject} • ${material.educationLevel} • ${material.topic.take(30)}", MARGIN_X, 16f, miniHeaderPaint)
                yPos = 36f
            }

            // Draw line items for this page
            val pageLines = pagesToDraw.getOrNull(pageIdx) ?: emptyList()
            for (lineItem in pageLines) {
                when (lineItem.type) {
                    LineType.DIVIDER -> {
                        val dividerPaint = Paint().apply {
                            color = Color.rgb(215, 230, 222)
                            strokeWidth = 1f
                        }
                        canvas.drawLine(MARGIN_X, yPos + (lineItem.height / 2f), PAGE_WIDTH - MARGIN_X, yPos + (lineItem.height / 2f), dividerPaint)
                    }
                    LineType.BULLET -> {
                        val bulletDotPaint = Paint().apply {
                            color = Color.rgb(0, 106, 78)
                            isAntiAlias = true
                        }
                        canvas.drawCircle(MARGIN_X + 6f, yPos - 3.5f, 2.5f, bulletDotPaint)
                        canvas.drawText(lineItem.text, MARGIN_X + 16f, yPos, lineItem.paint)
                    }
                    LineType.NUMBERED -> {
                        canvas.drawText(lineItem.text, MARGIN_X + 4f, yPos, lineItem.paint)
                    }
                    else -> {
                        canvas.drawText(lineItem.text, MARGIN_X, yPos, lineItem.paint)
                    }
                }
                yPos += lineItem.height
            }

            // Page Footer
            val footerLinePaint = Paint().apply {
                color = Color.rgb(220, 235, 228)
                strokeWidth = 0.8f
            }
            canvas.drawLine(MARGIN_X, PAGE_HEIGHT - 28f, PAGE_WIDTH - MARGIN_X, PAGE_HEIGHT - 28f, footerLinePaint)

            val footerTextPaint = Paint().apply {
                color = Color.rgb(130, 150, 140)
                textSize = 8.5f
                isAntiAlias = true
            }
            canvas.drawText("TeacherMate NG — AI Assistant for Nigerian Teachers 🇳🇬", MARGIN_X, PAGE_HEIGHT - 16f, footerTextPaint)

            val pageNumberStr = "Page ${pageIdx + 1} of $totalPages"
            val numWidth = footerTextPaint.measureText(pageNumberStr)
            canvas.drawText(pageNumberStr, PAGE_WIDTH - MARGIN_X - numWidth, PAGE_HEIGHT - 16f, footerTextPaint)

            pdfDocument.finishPage(page)
        }

        // Save PDF to cache / documents
        val cleanSubject = material.subject.replace(Regex("[^a-zA-Z0-9]"), "_")
        val cleanLevel = material.educationLevel.replace(Regex("[^a-zA-Z0-9]"), "_")
        val fileName = "TeacherMate_${cleanSubject}_${cleanLevel}_${System.currentTimeMillis()}.pdf"

        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: context.filesDir
        val file = File(storageDir, fileName)

        return try {
            val fos = FileOutputStream(file)
            pdfDocument.writeTo(fos)
            fos.close()
            pdfDocument.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            pdfDocument.close()
            null
        }
    }

    private fun wrapText(text: String, paint: Paint, maxWidth: Float): List<String> {
        if (text.isBlank()) return emptyList()
        val words = text.split(" ")
        val lines = mutableListOf<String>()
        var currentLine = StringBuilder()

        for (word in words) {
            val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
            val width = paint.measureText(testLine)
            if (width > maxWidth) {
                if (currentLine.isNotEmpty()) {
                    lines.add(currentLine.toString())
                    currentLine = StringBuilder(word)
                } else {
                    // Single word is longer than maxWidth
                    lines.add(word)
                    currentLine = StringBuilder()
                }
            } else {
                currentLine = StringBuilder(testLine)
            }
        }

        if (currentLine.isNotEmpty()) {
            lines.add(currentLine.toString())
        }

        return lines
    }

    fun sharePdf(context: Context, file: File, title: String) {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, "Here is the teaching material for $title generated via TeacherMate NG.")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "Share Teaching Material PDF"))
    }

    fun openPdf(context: Context, file: File) {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val viewIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            context.startActivity(viewIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "PDF saved to: ${file.name}", Toast.LENGTH_LONG).show()
        }
    }
}

private enum class LineType {
    H1, H2, H3, BODY, BULLET, NUMBERED, DIVIDER, EMPTY
}

private data class PdfLineItem(
    val type: LineType,
    val text: String,
    val paint: Paint,
    val height: Float
)
