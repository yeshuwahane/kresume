package com.cyphernerd.resume.domain.usecase

import javax.inject.Inject

class AtsScoringUseCase @Inject constructor() {

    suspend fun execute(resumeText: String, jobDescription: String): Result<Int> {
        return try {
            val score = calculateScore(resumeText, jobDescription)
            Result.success(score)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun calculateScore(resumeText: String, jobDescription: String): Int {
        val keywordScore = keywordMatchingScore(resumeText, jobDescription) * 0.25 // 25%
        val skillScore = skillMatchingScore(resumeText, jobDescription) * 0.15 // 15%
        val formattingScore = checkFormatting(resumeText) * 0.10 // 10%
        val readabilityScore = calculateReadability(resumeText) * 0.10 // 10%
        val lengthScore = checkLength(resumeText) * 0.10 // 10%
        val actionVerbScore = checkActionVerbs(resumeText) * 0.10 // 10%
        val grammarScore = checkGrammar(resumeText) * 0.05 // 5%
        val bulletPointScore = checkBulletPoints(resumeText) * 0.05 // 5%
        val jobTitleRelevanceScore = jobTitleRelevance(resumeText, jobDescription) * 0.05 // 5%
        val experienceScore = calculateExperience(resumeText) * 0.05 // 5%

        val finalScore = (keywordScore + skillScore + formattingScore + readabilityScore +
                lengthScore + actionVerbScore + grammarScore + bulletPointScore +
                jobTitleRelevanceScore + experienceScore).toInt()

        return finalScore.coerceIn(0, 100)
    }

    // 1. Keyword Matching (25%)
    private fun keywordMatchingScore(resume: String, jobDescription: String): Int {
        val jobKeywords = jobDescription.lowercase().split("\\s+".toRegex()).toSet()
        val resumeWords = resume.lowercase().split("\\s+".toRegex())

        val matchedKeywords = resumeWords.count { jobKeywords.contains(it) }
        val matchPercentage = (matchedKeywords.toDouble() / jobKeywords.size) * 100
        return matchPercentage.toInt().coerceIn(0, 100)
    }

    // 2. Skill Matching (15%)
    private fun skillMatchingScore(resume: String, jobDescription: String): Int {
        val skills = listOf(
            "Java", "Kotlin", "Android", "SQL", "REST API", "Git", "Agile",
            "React", "React Native", "GitHub", "Docker", "GraphQL", "Apollo Client",
            "CI/CD", "AWS", "Firebase", "Machine Learning", "TensorFlow", "PyTorch"
        )

        val foundSkills = skills.count { resume.contains(it, ignoreCase = true) }
        return (foundSkills.toDouble() / skills.size * 100).toInt().coerceIn(0, 100)
    }

    // 3. Formatting Score (10%)
    private fun checkFormatting(resume: String): Int {
        val sections = listOf("experience", "education", "skills", "certifications", "contact", "projects")
        val foundSections = sections.count { resume.contains(it, ignoreCase = true) }
        return (foundSections.toDouble() / sections.size * 100).toInt().coerceIn(0, 100)
    }

    // 4. Readability Score (10%)
    private fun calculateReadability(text: String): Int {
        val sentenceCount = text.split(Regex("[.!?]")).size
        val wordCount = text.split("\\s+".toRegex()).size
        val avgWordsPerSentence = if (sentenceCount > 0) wordCount / sentenceCount else 1

        return when {
            avgWordsPerSentence < 12 -> 100
            avgWordsPerSentence < 18 -> 80
            else -> 50
        }
    }

    // 5. Length Score (10%)
    private fun checkLength(resume: String): Int {
        val wordCount = resume.split("\\s+".toRegex()).size
        return when {
            wordCount in 300..800 -> 100
            wordCount in 150..299 || wordCount in 801..1000 -> 70
            else -> 40
        }
    }

    // 6. Action Verbs Check (10%)
    private fun checkActionVerbs(resume: String): Int {
        val actionVerbs = listOf(
            "developed", "designed", "implemented", "led", "managed", "optimized",
            "analyzed", "engineered", "collaborated", "orchestrated", "enhanced",
            "delivered", "automated", "launched"
        )

        val matchedVerbs = actionVerbs.count { resume.contains(it, ignoreCase = true) }
        return (matchedVerbs.toDouble() / actionVerbs.size * 100).toInt().coerceIn(0, 100)
    }

    // 7. Grammar Check (5%)
    private fun checkGrammar(resume: String): Int {
        val commonErrors = listOf(
            "their", "there", "they're", "your", "you're", "its", "it's",
            "affect", "effect", "loose", "lose", "then", "than"
        )

        val incorrectUsages = commonErrors.count { resume.contains(it, ignoreCase = true) }
        return (100 - (incorrectUsages * 5)).coerceIn(0, 100)
    }

    // 8. Bullet Points Check (5%)
    private fun checkBulletPoints(resume: String): Int {
        val bulletPointPattern = Regex("(?m)^[-•]")
        val bulletPointCount = bulletPointPattern.findAll(resume).count()
        return when {
            bulletPointCount >= 5 -> 100 // Well-structured resume
            bulletPointCount in 2..4 -> 70
            else -> 40 // Poorly formatted
        }
    }

    // 9. Job Title Relevance Check (5%)
    private fun jobTitleRelevance(resume: String, jobDescription: String): Int {
        val jobTitleRegex = Regex("(?i)(software engineer|android developer|data scientist|project manager)")
        val jobTitleMatches = jobTitleRegex.find(resume)?.value ?: ""
        return if (jobTitleMatches.isNotEmpty() && jobDescription.contains(jobTitleMatches, ignoreCase = true)) 100 else 50
    }

    // 10. Experience Calculation (5%)
    private fun calculateExperience(resume: String): Int {
        val yearsPattern = Regex("\\b(\\d{1,2})\\+?\\s+(years?|yrs?)\\b", RegexOption.IGNORE_CASE)
        val experienceYears = yearsPattern.find(resume)?.groups?.get(1)?.value?.toIntOrNull() ?: 0

        return when {
            experienceYears >= 5 -> 100
            experienceYears in 2..4 -> 80
            experienceYears == 1 -> 50
            else -> 30 // No experience mentioned
        }
    }
}
