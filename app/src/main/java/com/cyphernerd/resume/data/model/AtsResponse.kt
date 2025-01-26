package com.cyphernerd.resume.data.model



data class AtsResponse(val choices: List<Choice>) {
    data class Choice(val text: String)
}