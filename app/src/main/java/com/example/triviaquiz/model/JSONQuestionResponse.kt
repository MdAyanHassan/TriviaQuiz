package com.example.triviaquiz.model

/**
 * Receives the JSON Question request
 */

data class JSONQuestionResponse(
    val response_code: Int,
    val results: List<QuestionResponse>
)
