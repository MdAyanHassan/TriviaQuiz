package com.example.triviaquiz.model

import android.content.Context
import android.content.SharedPreferences

/**
 * Helps in persistence of highest score scored by user even if the activity is killed
 */

object SharedPreferencesManager {

    private const val PREF_KEY_NAME = "MySharedPreferences"

    private const val SCORE_KEY_NAME = "SCORE_KEY"

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {

        sharedPreferences = context.getSharedPreferences(PREF_KEY_NAME, Context.MODE_PRIVATE)

    }

    private fun saveScore(score: Int) {
        sharedPreferences.edit().putInt(SCORE_KEY_NAME, score).apply()
    }

    fun updateScore(score: Int) {
        val prevScore = sharedPreferences.getInt(SCORE_KEY_NAME, 0)

        if (score > prevScore) {
            saveScore(score)
        }
    }

    fun getScore(): Int {
        return sharedPreferences.getInt(SCORE_KEY_NAME, 0)
    }
}