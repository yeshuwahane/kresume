package com.cyphernerd.resume.data.network

import retrofit2.http.Body
import retrofit2.http.POST


interface OpenAIApi {
    @POST("engines/davinci-codex/completions")
    suspend fun getATSFeedback(@Body request: AtsRequest): AtsResponse
}

data class AtsRequest(val prompt: String, val maxTokens: Int = 150)

data class AtsResponse(val choices: List<Choice>) {
    data class Choice(val text: String)
}