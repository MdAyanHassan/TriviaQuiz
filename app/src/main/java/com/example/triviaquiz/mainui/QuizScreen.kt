package com.example.triviaquiz.mainui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircleOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.triviaquiz.viewmodels.QuizViewModel
import com.example.triviaquiz.data.DataSource
import com.example.triviaquiz.model.Question
import com.example.triviaquiz.ui.theme.TriviaQuizTheme
import com.example.triviaquiz.R

/**
 * Composable which shows our main app
 */

@Composable
fun App(quizViewModel: QuizViewModel = viewModel()) {

    if (!quizViewModel.isGameStarted) {
        if (!quizViewModel.isLoading) {
            MainScreenLayout()
        } else {
            LoadingScreen()
        }
    } else {
        QuizScreen()
    }

}

/**
 * Composable which shows the loading screen
 */

@Composable
fun LoadingScreen(quizViewModel: QuizViewModel = viewModel()) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(id = R.string.load_text),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold
        )

        if (!quizViewModel.isLoading) {
            return
        }

        CircularProgressIndicator(
            modifier = Modifier
                .width(64.dp)
                .padding(16.dp)
        )
    }
}

/**
 * Composable which shows our main quiz screen which includes the
 * Question
 * Options
 * Score
 * Index
 * Pause
 */

@Composable
fun QuizScreen(quizViewModel: QuizViewModel = viewModel()) {

    val quizUiState by quizViewModel.uiState.collectAsState()

    if (quizUiState.isGameOver) {
        FinalScoreDialog(score = quizUiState.score,
            onPlayAgain = { quizViewModel.onFinalDialogPlayAgain() },
            onExitGame = { quizViewModel.onExitGame() })
    } else {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            PauseIndexLayout(
                onPauseGame = { quizViewModel.onGamePause() },
                index = quizUiState.index
            )
            ScoreTextLayout(score = quizUiState.score)
            QuestionCardLayout(question = quizUiState.currentQuestion)
            OptionsLayout(question = quizUiState.currentQuestion,
                onOptionChose = { quizViewModel.checkUserAnswer(it) })

        }
        if (quizUiState.isGamePaused) {
            PauseMenuLayout(onResumeGame = { quizViewModel.onResumeGame() },
                onExitGame = { quizViewModel.onExitGame() })
        }
    }
}

/**
 * Composable to display final scorecard
 */

@Composable
fun FinalScoreDialog(
    score: Int,
    onPlayAgain: () -> Unit,
    onExitGame: () -> Unit,
    modifier: Modifier = Modifier
) {

    AlertDialog(
        onDismissRequest = { onExitGame() },
        title = { Text(text = stringResource(id = R.string.congrats_text)) },
        text = { Text(text = stringResource(id = R.string.score_final_text, score)) },
        confirmButton = {
            TextButton(onClick = onPlayAgain) {
                Text(text = stringResource(id = R.string.play_again_text))
            }
        },
        dismissButton = {
            TextButton(onClick = onExitGame) {
                Text(text = stringResource(id = R.string.exit_text))
            }
        },
        modifier = modifier
    )

}

/**
 * Composable to display pause menu
 */

@Composable
fun PauseMenuLayout(
    onResumeGame: () -> Unit,
    onExitGame: () -> Unit,
    modifier: Modifier = Modifier
) {

    AlertDialog(
        onDismissRequest = { onResumeGame() },
        title = {
            Text(text = stringResource(id = R.string.pause_menu_text))
        },
        text = {
            Text(stringResource(id = R.string.exit_ask))
        },
        dismissButton = {
            TextButton(onClick = onExitGame) {
                Text(text = stringResource(id = R.string.exit_text))
            }
        },
        confirmButton = {
            TextButton(onClick = onResumeGame) {
                Text(text = stringResource(id = R.string.resume_text))
            }
        },
        modifier = modifier
    )


}

/**
 * Composable to show the pause button
 * Index
 */

@Composable
fun PauseIndexLayout(
    onPauseGame: () -> Unit,
    index: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        IconButton(onClick = onPauseGame) {
            Icon(imageVector = Icons.Filled.PauseCircleOutline, contentDescription = null)
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(id = R.string.index_text, index, DataSource.TOTAL_API_QUESTIONS),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(4.dp)
        )
    }
}

/**
 * Composable to show the score
 */


@Composable
fun ScoreTextLayout(
    score: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.score_text, score),
        fontSize = 36.sp,
        fontStyle = FontStyle(R.font.montserrat_bold),
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    )
}

/**
 * Composable to show question
 */
@Composable
fun QuestionCardLayout(
    question: Question,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 16.dp, bottom = 16.dp, start = 4.dp, end = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = question.questionText,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(12.dp),
            style = MaterialTheme.typography.displayMedium
        )


    }
}

/**
 * Composable to show options
 */

@Composable
fun OptionsLayout(
    question: Question,
    onOptionChose: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val optionsList = question.incorrectAnswers.toMutableList()

    optionsList.add(question.correctAnswer)

    optionsList.shuffle()

    Column(
        modifier = modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 28.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        for (optionText in optionsList) {
            OutlinedButton(
                onClick = { onOptionChose(optionText) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = optionText,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

    }
}

/**
 * Composable to show the main screen ui where user may choose category and stuff
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenLayout(quizViewModel: QuizViewModel = viewModel()) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {

            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 56.dp)
            )

        }

        //For difficulty
        ExposedDropdownMenuBox(expanded = quizViewModel.isDifficultyExpanded,
            onExpandedChange = { quizViewModel.onDifficultyExpanded() }) {

            TextField(value = quizViewModel.difficultyChoice,
                label = {
                    Text(
                        text = stringResource(id = R.string.difficulty_text),
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                onValueChange = { },
                colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                readOnly = true,
                modifier = Modifier.menuAnchor(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = quizViewModel.isDifficultyExpanded) })

            ExposedDropdownMenu(
                expanded = quizViewModel.isDifficultyExpanded,
                onDismissRequest = { quizViewModel.onDifficultyDismiss() }) {
                quizViewModel.difficultyList.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it) },
                        onClick = { quizViewModel.onDifficultyItemClicked(it) },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }

        }

        Spacer(modifier = Modifier.height(8.dp))

        //For amount
        ExposedDropdownMenuBox(expanded = quizViewModel.isAmountExpanded,
            onExpandedChange = { quizViewModel.onAmountExpanded() }) {

            TextField(value = quizViewModel.amountChoice.toString(),
                label = {
                    Text(
                        text = stringResource(id = R.string.amount_text),
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                onValueChange = { },
                readOnly = true,
                modifier = Modifier.menuAnchor(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = quizViewModel.isAmountExpanded) })

            ExposedDropdownMenu(
                expanded = quizViewModel.isAmountExpanded,
                onDismissRequest = { quizViewModel.onAmountDismiss() }) {
                quizViewModel.amountList.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it.toString()) },
                        onClick = { quizViewModel.onAmountItemClicked(it) },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }

        }

        Spacer(modifier = Modifier.height(8.dp))

        //For category
        ExposedDropdownMenuBox(expanded = quizViewModel.isCategoryExpanded,
            onExpandedChange = { quizViewModel.onCategoryExpanded() }) {

            TextField(value = quizViewModel.categoryChoice.name,
                label = {
                    Text(
                        text = stringResource(id = R.string.category_text),
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                onValueChange = { },
                readOnly = true,
                modifier = Modifier.menuAnchor(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = quizViewModel.isCategoryExpanded) })

            ExposedDropdownMenu(
                expanded = quizViewModel.isCategoryExpanded,
                onDismissRequest = { quizViewModel.onCategoryDismiss() }) {
                quizViewModel.categoryList.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it.name) },
                        onClick = { quizViewModel.onCategoryItemClicked(it) },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }

        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = { quizViewModel.startQuiz() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {

            Text(
                text = stringResource(id = R.string.start_text),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

        }

        Text(
            text = stringResource(id = R.string.highest_score_text, quizViewModel.highestScore),
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(8.dp)
        )

    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenLayoutPreview() {
    TriviaQuizTheme {
        MainScreenLayout()
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun QuizScreenPreview() {
    TriviaQuizTheme {
        QuizScreen()
    }
}


