package com.example.triviaquiz.model

/**
 * Recieves the JSON Category request
 */

data class JSONCategoryResponse(
    val trivia_categories: List<Category>
)
