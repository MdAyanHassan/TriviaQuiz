package com.example.triviaquiz.model

/**
 * This data class helps in holding the information of a question
 */

data class Question(
    val questionText: String,
    val correctAnswer: String,
    val incorrectAnswers: MutableList<String>
)
