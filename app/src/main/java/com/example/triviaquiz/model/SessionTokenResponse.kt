package com.example.triviaquiz.model

/**
 * Receives the session token JSON Response
 */

data class SessionTokenResponse(
    val response_code: Int,
    val response_message: String,
    val token: String
)
