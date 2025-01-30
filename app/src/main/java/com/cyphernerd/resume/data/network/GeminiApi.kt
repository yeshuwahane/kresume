package com.cyphernerd.resume.data.network

import com.cyphernerd.resume.data.model.AtsRequest
import com.cyphernerd.resume.data.model.AtsResponse
import com.cyphernerd.resume.data.model.GeminiRequest
import com.cyphernerd.resume.data.model.GeminiResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path


interface GeminiApi {
    @POST("/v1beta/projects/{project}/locations/{location}/publishers/google/models/{model}:generateContent")
    suspend fun getGeminiFeedback(
        @Path("project") project: String,
        @Path("location") location: String,
        @Path("model") model: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}


