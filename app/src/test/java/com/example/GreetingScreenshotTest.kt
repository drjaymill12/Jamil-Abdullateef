package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.ui.screens.StructuredLessonPlanContent
import com.example.ui.theme.TeacherMateTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun lessonPlan_screenshot() {
    composeTestRule.setContent {
      TeacherMateTheme {
        StructuredLessonPlanContent(
          content = """
            ## 1. BEHAVIORAL OBJECTIVES
            By the end of the lesson, learners will solve linear equations.
            
            ## 2. INSTRUCTIONAL MATERIALS
            Graph sheet, ruler, chalkboard grid.
          """.trimIndent()
        )
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
  }
}
