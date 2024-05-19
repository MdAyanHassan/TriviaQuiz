package com.example.triviaquiz.model

/**
 * Holds the session token in this singleton so only one session token is generated
 * This helps in getting different questions in same category
 */

object SessionTokenManager {

    var sessionToken: String = ""

}