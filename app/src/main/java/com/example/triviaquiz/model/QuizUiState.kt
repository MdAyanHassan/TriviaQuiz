package com.example.triviaquiz.model

/**
 * This data class holds the current ui state of our app when quiz is running
 */

data class QuizUiState(
    val currentQuestion: Question = Question("", "", mutableListOf()),
    val score: Int = 0,
    val index: Int = 1,
    val isGameOver: Boolean = false,
    val isGamePaused: Boolean = false,
    val isCorrectChoice: Boolean = false
)