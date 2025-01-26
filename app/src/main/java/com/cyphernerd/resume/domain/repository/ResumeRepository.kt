package com.cyphernerd.resume.domain.repository

import com.cyphernerd.resume.data.model.AtsResponse

interface ResumeRepository {
    suspend fun fetchFeedback(prompt: String): AtsResponse

    suspend fun analyzeResume(pdfText: String): AtsResponse
}