package com.cyphernerd.resume.data.model

import com.google.gson.annotations.SerializedName


data class GeminiResponse(
    val predictions: List<Prediction>
)

data class Prediction(
    val content: ContentResponse
)

data class ContentResponse(
    val parts: List<PartResponse>
)
data class PartResponse(
    val text: String
)