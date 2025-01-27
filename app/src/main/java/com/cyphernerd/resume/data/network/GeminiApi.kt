package com.cyphernerd.resume.data.network

import com.cyphernerd.resume.data.model.AtsRequest
import com.cyphernerd.resume.data.model.AtsResponse
import com.cyphernerd.resume.data.model.GeminiRequest
import com.cyphernerd.resume.data.model.GeminiResponse
import retrofit2.http.Body
import retrofit2.http.POST



interface GeminiApi {
    @POST("projects/{project}/locations/{location}/endpoints/{endpoint}:generateContent")
    suspend fun getGeminiFeedback(
        @Body request: GeminiRequest,
        @retrofit2.http.Path("project") project: String,
        @retrofit2.http.Path("location") location: String,
        @retrofit2.http.Path("endpoint") endpoint: String
    ): GeminiResponse
}


