package com.cyphernerd.resume.domain.usecase

import com.cyphernerd.resume.data.repositoryimpl.ResumeRepositoryImpl
import com.cyphernerd.resume.domain.repository.ResumeRepository
import javax.inject.Inject


class AtsScoringUseCase @Inject constructor(
    private val resumeRepository: ResumeRepository
) {

    suspend fun execute(resumeText: String, jobDescription: String): Result<Int> {
        return try {
            val response = resumeRepository.analyzeResume(resumeText, jobDescription)
            val scoreText = response.predictions.firstOrNull()
                ?.content?.parts?.firstOrNull()?.text?.trim() ?: "0"
            val score = scoreText.toIntOrNull()?.coerceIn(0, 100) ?: 0
            Result.success(score)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
