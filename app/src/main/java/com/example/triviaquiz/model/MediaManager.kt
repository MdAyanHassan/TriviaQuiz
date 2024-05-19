package com.example.triviaquiz.model

import android.content.Context
import android.media.MediaPlayer
import com.example.triviaquiz.R

/**
 * Helps in playing the correct and incorrect sound to notify the user of whether
 * he choose correct answer or not
 */

object MediaManager {

    private lateinit var correctAnswerPlayer: MediaPlayer

    private lateinit var wrongAnswerPlayer: MediaPlayer

    fun init(context: Context) {
        correctAnswerPlayer = MediaPlayer.create(context, R.raw.correct)
        wrongAnswerPlayer = MediaPlayer.create(context, R.raw.wrong)
    }

    fun playCorrect() {
        correctAnswerPlayer.start()
    }

    fun playWrong() {
        wrongAnswerPlayer.start()
    }
}