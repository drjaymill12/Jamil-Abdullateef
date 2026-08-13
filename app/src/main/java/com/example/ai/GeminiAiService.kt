package com.example.ai

import android.util.Log
import com.example.BuildConfig
import com.example.model.MaterialCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class GenerationResult(
    val content: String,
    val isAiGenerated: Boolean,
    val notice: String? = null
)

object GeminiAiService {
    private const val TAG = "TeacherMateAi"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun generateMaterial(
        category: MaterialCategory,
        schoolLevel: String,
        subject: String,
        term: String,
        topic: String,
        subTopic: String = "",
        customInstructions: String = ""
    ): GenerationResult = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        val hasValidKey = apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY"

        if (!hasValidKey) {
            Log.d(TAG, "No valid Gemini API key found, generating via Curriculum Template Engine")
            val offlineContent = CurriculumTemplateEngine.generateOfflineContent(
                category = category,
                schoolLevel = schoolLevel,
                subject = subject,
                term = term,
                topic = topic,
                subTopic = subTopic,
                customNote = customInstructions
            )
            return@withContext GenerationResult(
                content = offlineContent,
                isAiGenerated = false,
                notice = "Generated using TeacherMate NG Built-in NERDC Curriculum Engine (Offline Mode)."
            )
        }

        try {
            val systemPrompt = NigerianCurriculumPromptBuilder.buildSystemPrompt()
            val userPrompt = NigerianCurriculumPromptBuilder.buildUserPrompt(
                category = category,
                schoolLevel = schoolLevel,
                subject = subject,
                term = term,
                topic = topic,
                subTopic = subTopic,
                customInstructions = customInstructions
            )

            val requestJson = JSONObject().apply {
                put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().put(JSONObject().put("text", systemPrompt)))
                })
                put("contents", JSONArray().put(JSONObject().apply {
                    put("parts", JSONArray().put(JSONObject().put("text", userPrompt)))
                }))
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.6)
                    put("topP", 0.95)
                    put("topK", 40)
                })
            }

            val requestBody = requestJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(requestBody)
                .build()

            val response = okHttpClient.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && responseBody != null) {
                val jsonObject = JSONObject(responseBody)
                val candidates = jsonObject.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val firstCandidate = candidates.getJSONObject(0)
                    val contentObj = firstCandidate.optJSONObject("content")
                    val parts = contentObj?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val text = parts.getJSONObject(0).optString("text")
                        if (text.isNotBlank()) {
                            return@withContext GenerationResult(
                                content = text,
                                isAiGenerated = true,
                                notice = "Generated via Gemini 3.5 AI — NERDC Nigerian Educational Specialist."
                            )
                        }
                    }
                }
            }

            Log.w(TAG, "Gemini API returned empty or non-200 response ($response), falling back to template engine")
            val offlineContent = CurriculumTemplateEngine.generateOfflineContent(
                category = category,
                schoolLevel = schoolLevel,
                subject = subject,
                term = term,
                topic = topic,
                subTopic = subTopic,
                customNote = customInstructions
            )
            GenerationResult(
                content = offlineContent,
                isAiGenerated = false,
                notice = "Generated using Offline NERDC Curriculum Engine."
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error contacting Gemini API: ${e.message}", e)
            val offlineContent = CurriculumTemplateEngine.generateOfflineContent(
                category = category,
                schoolLevel = schoolLevel,
                subject = subject,
                term = term,
                topic = topic,
                subTopic = subTopic,
                customNote = customInstructions
            )
            GenerationResult(
                content = offlineContent,
                isAiGenerated = false,
                notice = "Generated using Offline NERDC Curriculum Engine (Network Offline)."
            )
        }
    }
}
