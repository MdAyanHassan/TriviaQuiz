package com.example.triviaquiz.network

import com.example.triviaquiz.model.JSONCategoryResponse
import com.example.triviaquiz.model.JSONQuestionResponse
import com.example.triviaquiz.model.SessionTokenResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * The network interface consists of three methods to access
 * Question Response
 * Category Response
 * Token Response
 */

interface QuestionApi {

    @GET("api.php")
    suspend fun getQuestions(@Query("amount") amount: Int = 10,
        @Query("category") category: Int = 10,
        @Query("difficulty") difficulty: String = "easy",
        @Query("token") token: String): Response<JSONQuestionResponse>

    @GET("api_category.php")
    suspend fun getCategory(): Response<JSONCategoryResponse>

    @GET("api_token.php")
    suspend fun getToken(@Query("command") command: String = "request"): Response<SessionTokenResponse>

}