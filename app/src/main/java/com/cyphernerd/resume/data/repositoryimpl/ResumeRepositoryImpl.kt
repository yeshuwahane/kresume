package com.cyphernerd.resume.data.repositoryimpl

import android.util.Log
import com.cyphernerd.resume.data.model.AtsRequest
import com.cyphernerd.resume.data.model.AtsResponse
import com.cyphernerd.resume.data.model.Content
import com.cyphernerd.resume.data.model.GeminiRequest
import com.cyphernerd.resume.data.model.GeminiResponse
import com.cyphernerd.resume.data.model.Part
import com.cyphernerd.resume.data.network.GeminiApi
import com.cyphernerd.resume.domain.repository.ResumeRepository
import javax.inject.Inject


class ResumeRepositoryImpl @Inject constructor(
    private val geminiApi: GeminiApi
) : ResumeRepository {

    private val projectId = "k-resume"
    private val location = "global"
    private val model = "gemini-pro"


    override suspend fun analyzeResume(resumeText: String, jobDescription: String): GeminiResponse {
        val promptText = """
            I want you to act as an Automated Applicant Tracking System (ATS) expert. Analyze the provided resume text in relation to the job description. Provide a score out of 100 to reflect how well the candidate is matching the job description criteria. Return only the score, nothing else.
        
            Resume Text:
            $resumeText
        
            Job Description:
            $jobDescription

            ATS Score:
        """.trimIndent()

        val request = GeminiRequest(
            contents = listOf(
                Content(parts = listOf(
                    Part(text = promptText)
                ))
            )
        )

        return try {
            val response = geminiApi.getGeminiFeedback(
                request = request,
                project = projectId,
                location = location,
                model = model
            )
            Log.d("Gemini_API", response.predictions[0].content.parts[0].text)
            response
        } catch (e: Exception) {
            Log.e("Gemini_API", e.message.toString())
            throw e
        }
    }
}