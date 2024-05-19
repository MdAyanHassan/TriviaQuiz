package com.example.triviaquiz.data

import com.example.triviaquiz.model.Question

/**
 * Object which holds all the questions it received from the api request
 * which are then retrieved by our view model
 * and displayed to UI
 */

object DataSource {

    val apiQuestionsResponse = mutableListOf<Question>()

    var TOTAL_API_QUESTIONS = apiQuestionsResponse.size

}