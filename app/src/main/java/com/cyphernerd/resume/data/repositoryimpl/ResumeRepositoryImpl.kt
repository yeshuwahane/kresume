package com.cyphernerd.resume.data.repositoryimpl

import com.cyphernerd.resume.data.model.AtsRequest
import com.cyphernerd.resume.data.model.AtsResponse
import com.cyphernerd.resume.data.network.GeminiApi
import com.cyphernerd.resume.domain.repository.ResumeRepository
import javax.inject.Inject


class ResumeRepositoryImpl @Inject constructor(val api: GeminiApi) : ResumeRepository {

    override suspend fun fetchFeedback(prompt: String): AtsResponse {
        return api.getATSFeedback(AtsRequest(prompt))
    }

    override suspend fun analyzeResume(pdfText: String): AtsResponse {
        return api.getATSFeedback(AtsRequest("Analyze this resume: $pdfText"))
    }
}