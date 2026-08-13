package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.NigeriaGreen
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.TextSecondaryLight

@Composable
fun TeacherHeader(
    savedCount: Int,
    onOpenLibrary: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("teacher_header"),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, BorderSubtle),
        tonalElevation = 0.dp,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Brand Header with Geometric Balance typography
                Column {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = GreenPrimary, fontWeight = FontWeight.Bold)) {
                                append("TeacherMate ")
                            }
                            withStyle(SpanStyle(color = OrangeAccent, fontWeight = FontWeight.ExtraBold)) {
                                append("NG")
                            }
                        },
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            letterSpacing = (-0.5).sp
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "AI ASSISTANT FOR EDUCATORS • NERDC ALIGNED",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.8.sp,
                            color = TextSecondaryLight
                        )
                    )
                }

                // Profile Avatar / Saved Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Library / Saved button with Badge
                    IconButton(
                        onClick = onOpenLibrary,
                        modifier = Modifier.testTag("header_library_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (savedCount > 0) {
                                    Badge(
                                        containerColor = OrangeAccent,
                                        contentColor = Color.White
                                    ) {
                                        Text("$savedCount")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = "Saved Materials",
                                tint = GreenPrimary
                            )
                        }
                    }

                    // Avatar Circle Badge "TM"
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(GreenPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "TM",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 13.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NigerianFlagBadge() {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = Color(0xFFE8F5E9),
        modifier = Modifier.height(18.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 4.dp, height = 10.dp)
                    .background(NigeriaGreen)
            )
            Box(
                modifier = Modifier
                    .size(width = 4.dp, height = 10.dp)
                    .background(Color.White)
            )
            Box(
                modifier = Modifier
                    .size(width = 4.dp, height = 10.dp)
                    .background(NigeriaGreen)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "NG",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 9.sp
                ),
                color = GreenPrimary
            )
        }
    }
}

