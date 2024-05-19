package com.example.triviaquiz.model

/**
 * This receives the Question JSON Response
 */

data class QuestionResponse(
    val type: String,
    val difficulty: String,
    val category: String,
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>
)
