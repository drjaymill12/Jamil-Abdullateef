package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MaterialCategory
import com.example.ui.theme.BadgeAmber
import com.example.ui.theme.BadgeBlue
import com.example.ui.theme.BadgeCyan
import com.example.ui.theme.BadgeEmerald
import com.example.ui.theme.BadgeIndigo
import com.example.ui.theme.BadgeLime
import com.example.ui.theme.BadgePurple
import com.example.ui.theme.BadgeRose
import com.example.ui.theme.BadgeTeal
import com.example.ui.theme.BorderCard
import com.example.ui.theme.TextSecondaryLight

@Composable
fun MaterialCategoryCard(
    category: MaterialCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (badgeBg, subtitleTag) = when (category) {
        MaterialCategory.LESSON_PLAN -> Pair(BadgeBlue, "PDF READY")
        MaterialCategory.EXAM_QUESTIONS -> Pair(BadgePurple, "MULTIPLE CHOICE")
        MaterialCategory.SCHEME_OF_WORK -> Pair(BadgeTeal, "TERM ALIGNMENT")
        MaterialCategory.LEARNING_OBJECTIVES -> Pair(BadgeAmber, "BLOOM'S TAXONOMY")
        MaterialCategory.MARKING_SCHEME -> Pair(BadgeEmerald, "RUBRIC STYLE")
        MaterialCategory.RESULT_GENERATOR -> Pair(BadgeIndigo, "CLASS BROAD-SHEET")
        MaterialCategory.QUIZ_GENERATOR -> Pair(BadgeRose, "AI POWERED")
        MaterialCategory.NOTES_GENERATOR -> Pair(BadgeCyan, "SUMMARIES")
        MaterialCategory.CURRICULUM_GUIDE -> Pair(BadgeLime, "NATIONAL STANDARD")
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("category_card_${category.name.lowercase()}")
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp,
            pressedElevation = 2.dp
        ),
        border = BorderStroke(1.dp, BorderCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Category Geometric Badge with Pastel Background
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(badgeBg),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category.iconEmoji,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text = category.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Subtitle Tag
            Text(
                text = subtitleTag,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 9.5.sp,
                    letterSpacing = 0.5.sp
                ),
                color = TextSecondaryLight,
                maxLines = 1
            )
        }
    }
}

