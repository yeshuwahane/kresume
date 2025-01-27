package com.cyphernerd.resume.domain.repository

import com.cyphernerd.resume.data.model.AtsResponse
import com.cyphernerd.resume.data.model.GeminiResponse

interface ResumeRepository {
    suspend fun analyzeResume(resumeText: String, jobDescription: String): GeminiResponse
}