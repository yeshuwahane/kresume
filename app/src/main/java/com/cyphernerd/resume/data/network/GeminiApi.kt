package com.cyphernerd.resume.data.network

import retrofit2.http.Body
import retrofit2.http.POST


interface GeminiApi {
    @POST("engines/davinci-codex/completions")
    suspend fun getATSFeedback(@Body request: AtsRequest): AtsResponse
}