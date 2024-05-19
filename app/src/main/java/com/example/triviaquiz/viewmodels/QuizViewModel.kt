package com.example.triviaquiz.viewmodels

import android.text.Html
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triviaquiz.data.DataSource
import com.example.triviaquiz.model.Category
import com.example.triviaquiz.model.Difficulty
import com.example.triviaquiz.model.MediaManager
import com.example.triviaquiz.model.Question
import com.example.triviaquiz.network.QuestionApi
import com.example.triviaquiz.model.QuizUiState
import com.example.triviaquiz.model.SessionTokenManager
import com.example.triviaquiz.model.SharedPreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
//import android.R

//Why is above line giving unresolved reference some times?
/**
 * This view model helps in doing all state changes and holding them and updating whenever the Ui
 * requires
 */

private const val BASE_URL = "https://opentdb.com/"

private const val TAG = "MainActivity"

class QuizViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private var isChoiceCorrect by mutableStateOf(false)

    var highestScore: Int = 0
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isGameStarted by mutableStateOf(false)
        private set

    val difficultyList = listOf(Difficulty.easy.toString().replaceFirstChar { it.uppercase() },
        Difficulty.medium.toString().replaceFirstChar { it.uppercase() },
        Difficulty.hard.toString().replaceFirstChar { it.uppercase() })

    var isDifficultyExpanded by mutableStateOf(false)
    var difficultyChoice by mutableStateOf("Easy")
        private set

    val amountList = listOf(5, 10, 15, 20)

    var isAmountExpanded by mutableStateOf(false)
    var amountChoice by mutableStateOf(10)
        private set

    val categoryList = mutableListOf<Category>()

    var isCategoryExpanded by mutableStateOf(false)
    var categoryChoice by mutableStateOf(Category(9, "General Knowledge"))
        private set

    private var currIndex = 0

    private fun updateHighestScoreInMemory() {

        viewModelScope.launch {
            SharedPreferencesManager.updateScore(_uiState.value.score)
        }

        highestScore = SharedPreferencesManager.getScore()
    }

    fun startQuiz() {

        viewModelScope.launch {
            isLoading = true
            val apiService = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(QuestionApi::class.java)

            val response = try {
                apiService.getQuestions(
                    amount = amountChoice,
                    category = categoryChoice.id,
                    difficulty = difficultyChoice.replaceFirstChar { it.lowercase() },
                    token = SessionTokenManager.sessionToken
                )
            } catch (i: IOException) {
                Log.e(TAG, "Not connected to internet")
                return@launch
            } catch (e: HttpException) {
                Log.e(TAG, "Unable to get response")
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                response.body()!!.results.forEach {
                    DataSource.apiQuestionsResponse.add(
                        Question(
                            convertHTMLTextToNormalText(it.question),
                            convertHTMLTextToNormalText(it.correct_answer),
                            it.incorrect_answers.toMutableList().map { answer ->
                                convertHTMLTextToNormalText(answer)
                            }.toMutableList()
                        )
                    )
                }
                isLoading = false
                isGameStarted = true
                DataSource.TOTAL_API_QUESTIONS = DataSource.apiQuestionsResponse.size
                resetGame()
            } else {
                Log.e(TAG, "Response not successful")
                isLoading = false
                isGameStarted = false
            }

        }

    }

    private fun convertHTMLTextToNormalText(text: String): String {
        return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY).toString()
    }

    fun onDifficultyExpanded() {
        isDifficultyExpanded = !isDifficultyExpanded
    }

    fun onDifficultyDismiss() {
        isDifficultyExpanded = false
    }

    fun onDifficultyItemClicked(difficulty: String) {
        difficultyChoice = difficulty
        onDifficultyDismiss()
    }

    fun onAmountExpanded() {
        isAmountExpanded = !isAmountExpanded
    }

    fun onAmountDismiss() {
        isAmountExpanded = false
    }

    fun onAmountItemClicked(amount: Int) {
        amountChoice = amount
        onAmountDismiss()
    }

    fun onCategoryExpanded() {
        isCategoryExpanded = !isCategoryExpanded
    }

    fun onCategoryDismiss() {
        isCategoryExpanded = false
    }

    fun onCategoryItemClicked(category: Category) {
        categoryChoice = category
        onCategoryDismiss()
    }

    fun onGamePause() {
        _uiState.update {
            it.copy(isGamePaused = true)
        }
    }

    fun onResumeGame() {
        _uiState.update {
            it.copy(isGamePaused = false)
        }
    }

    fun onExitGame() {
        _uiState.update {
            it.copy(isGameOver = true, isGamePaused = false)
        }
        isGameStarted = false
        isLoading = false
        currIndex = 0
        isChoiceCorrect = false
        DataSource.apiQuestionsResponse.clear()
        DataSource.TOTAL_API_QUESTIONS = 0
    }

    fun checkUserAnswer(userAnswer: String) {
        isChoiceCorrect = userAnswer == _uiState.value.currentQuestion.correctAnswer

        if (isChoiceCorrect) {
            MediaManager.playCorrect()
        }
        else {
            MediaManager.playWrong()
        }

        updateGameState()
    }

    private fun generateNewQuestion(): Question {
        val question = DataSource.apiQuestionsResponse[currIndex]
        currIndex++
        return question
    }

    private fun populateCategoryList() {

        viewModelScope.launch {
            val apiService = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(QuestionApi::class.java)

            val response = try {
                apiService.getCategory()

            } catch (i: IOException) {
                Log.e(TAG, "Not connected to internet")
                return@launch
            } catch (e: HttpException) {
                Log.e(TAG, "Unable to get response")
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                response.body()!!.trivia_categories.forEach {
                    categoryList.add(it)
                }
            } else {
                Log.e(TAG, "Response not successful")
            }
        }

    }

    private fun updateGameState() {

        if (currIndex == DataSource.TOTAL_API_QUESTIONS) {
            _uiState.update {
                it.copy(isGameOver = true,
                    score = if (isChoiceCorrect) _uiState.value.score.plus(10) else _uiState.value.score
                )
            }
            updateHighestScoreInMemory()
        } else {
            _uiState.update {
                it.copy(
                    currentQuestion = generateNewQuestion(),
                    index = it.index.inc(),
                    isCorrectChoice = isChoiceCorrect,
                    score = if (isChoiceCorrect) _uiState.value.score.plus(10) else _uiState.value.score
                )
            }
        }
        isChoiceCorrect = false
    }

    fun onFinalDialogPlayAgain() {
        _uiState.update {
            it.copy(isGameOver = true, isGamePaused = false)
        }
        isGameStarted = false
        isChoiceCorrect = false
        currIndex = 0
        isLoading = false
        DataSource.apiQuestionsResponse.clear()
        DataSource.TOTAL_API_QUESTIONS = 0
    }

    private fun resetGame() {
        isChoiceCorrect = false
        currIndex = 0
        _uiState.value = QuizUiState(generateNewQuestion())
    }

    private fun generateSessionToken() {
        viewModelScope.launch {
            val apiService = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(QuestionApi::class.java)

            val response = try {
                apiService.getToken()
            } catch (i: IOException) {
                Log.e(TAG, "Not connected to internet")
                return@launch
            } catch (e: HttpException) {
                Log.e(TAG, "Unable to get response")
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                SessionTokenManager.sessionToken = response.body()!!.token
            }
            else {
                Log.e(TAG, "Response not successful")
            }
        }
    }
    init {

        highestScore = SharedPreferencesManager.getScore()

        populateCategoryList()

        generateSessionToken()
    }
}