package com.example.enggo.ui.lesson

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.enggo.R
import com.example.enggo.data.service.ExerciseService
import com.example.enggo.data.service.TheoryService
import com.example.enggo.model.course.Lesson
import com.example.enggo.model.course.Theory
import com.example.enggo.ui.theme.EngGoTheme
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
internal fun LessonRoute(
    lessonName: String?,
    lessonId: Int,
    onBackPress: () -> Unit,
    onGoToExercise: (Int, String) -> Unit,
) {
    // TODO()
    LessonScreen(lessonName = lessonName, lessonId = lessonId, onGoToExercise = onGoToExercise)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonScreen(
    lessonName: String?,
    lessonId: Int,
    onGoToExercise: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val topBarState = rememberTopAppBarState()

    Scaffold(
        topBar = {
            LessonTopAppBar(lessonName ?: "Theory")
        },
    ) { paddingValues ->
        if (lessonName != null) {
            TheoryScreen(lessonName = lessonName, lessonId = lessonId, onGoToExercise = onGoToExercise, modifier = Modifier.padding(paddingValues))
        }
    }
}

suspend fun getTheory(id: Int, coroutineScope: CoroutineScope): Theory? {
    var theory: Theory? = null
    coroutineScope.launch {
        val theoryService = TheoryService(FirebaseFirestore.getInstance())
        theory = theoryService.getTheoryByLessonId(id)
    }.join()
    return theory
}

@Composable
fun TheoryScreen(
    lessonName: String,
    lessonId: Int,
    onGoToExercise: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var theory by remember { mutableStateOf<Theory?>(null) }

    LaunchedEffect(lessonId) {
        theory = getTheory(id = lessonId, coroutineScope = coroutineScope)
    }

    Column(
        modifier = Modifier
            .padding(
                vertical = dimensionResource(R.dimen.padding_small),
                horizontal = dimensionResource(R.dimen.padding_medium)
            )
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        theory?.let {
            Text(
                text = contentFormatter(it.content),

                modifier = modifier
                //.fillMaxWidth()
                //.padding(dimensionResource(R.dimen.padding_medium))
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                //.fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_small))
        ) {
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {onGoToExercise(lessonId, lessonName)},
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),

            ) {
                Text(
                    text = "Go to exercise",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Composable
internal fun ExerciseRoute(
    lessonName: String?,
    lessonId: Int,
    exerciseIndex: Int,
    onBackPress: () -> Unit,
    onNextLessonPress: (Int, String, Int) -> Unit,
    onUnitPress: () -> Unit,
    navController: NavController
) {
    Scaffold(
        topBar = {
            LessonTopAppBar(lessonName ?: "Exercise")
        },
    ) { paddingValues ->
        if (lessonName != null) {
            ExerciseScreen(
                lessonName = lessonName,
                lessonId = lessonId,
                exerciseIndex = exerciseIndex,
                onBackPress = onBackPress,
                onNextLessonPress = onNextLessonPress,
                onUnitPress = onUnitPress,
                navController = navController,
                modifier = Modifier.padding(paddingValues))
        }
    }
}

@Composable
fun ExerciseScreen(
    lessonName: String,
    lessonId: Int,
    exerciseIndex: Int,
    onBackPress: () -> Unit,
    onNextLessonPress: (Int, String, Int) -> Unit,
    onUnitPress: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val exerciseService = ExerciseService(FirebaseFirestore.getInstance())
    val lessonViewModel : LessonViewModel = viewModel(factory = LessonViewModelFactory(exerciseService, lessonId))

    val exercises by lessonViewModel.exercises.collectAsState()

    val currentExercise = exercises.getOrNull(exerciseIndex)

    if (currentExercise != null) {
        var userAnswers by remember { mutableStateOf(List(currentExercise.questions.size) { "" }) }
        var correctCount by remember { mutableStateOf(0) }
        var showResult by remember { mutableStateOf(false) }

        Column(
            modifier = modifier
                .padding(
                    vertical = dimensionResource(R.dimen.padding_small),
                    horizontal = dimensionResource(R.dimen.padding_medium)
                )
                .verticalScroll(rememberScrollState()),
            //verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            Text(
                text = currentExercise.exercise_title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_small))
            )
            currentExercise.questions.forEachIndexed { index, question ->
                Text(
                    text = question.question,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small))
                )
                OutlinedTextField(
                    value = userAnswers[index], // Bind to the respective answer
                    onValueChange = { newAnswer ->
                        userAnswers = userAnswers.toMutableList().apply { this[index] = newAnswer }
                    },
                    label = { Text("Your answer") },
                    modifier = Modifier.fillMaxWidth(), // TODO
                    trailingIcon = {
                        if (showResult) {
                            val icon = if (userAnswers[index].trim().equals(question.answer, ignoreCase = true)) {
                                Icons.Default.Check // Correct answer icon (V)
                            } else {
                                Icons.Default.Close // Wrong answer icon (X)
                            }
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (userAnswers[index].trim().equals(question.answer, ignoreCase = true)) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.error)
                        }
                    }
                )

                if (showResult && userAnswers[index].trim() != question.answer) {
                    Text(text = "Correct answer: ${question.answer}", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyLarge)
                }
            }

            Button(
                onClick = {
                    correctCount = 0
                    showResult = true
                    currentExercise.questions.forEachIndexed { index, question ->
                        if (userAnswers[index].trim().equals(question.answer, ignoreCase = true)) {
                            correctCount++
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = dimensionResource(R.dimen.padding_small))
            ) {
                Text(
                    text = "Check Answer",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (showResult) {
                Text(
                    text = "$correctCount out of ${currentExercise.questions.size} correct!",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            if (exerciseIndex < exercises.size - 1) {
                Button(
                    onClick = {
                        onNextLessonPress(lessonId, lessonName, exerciseIndex + 1)
                    },
                    modifier = Modifier.fillMaxWidth().padding(top = dimensionResource(R.dimen.padding_small))
                ) {
                    Text(
                        text = "Next Exercise",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            } else {
                Button(
                    onClick = {
                        val encodedLessonName = URLEncoder.encode(lessonName, StandardCharsets.UTF_8.toString())
                        //onBackPress()
                        navController.navigate("lesson/$encodedLessonName/$lessonId") {
                            popUpTo("lesson/$encodedLessonName/$lessonId") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(top = dimensionResource(R.dimen.padding_small))
                ) {
                    Text(
                        text = "Finish and go back to theory",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonTopAppBar(
    lessonName: String,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = lessonName,
                    style = MaterialTheme.typography.displaySmall
                )
            }
        },
        modifier = modifier
    )
}

val sampleTheoryData = Theory(
    theory_id = 0,
    lesson_id = 0,
    content = "@positive@\n" +
            "I *am* (I*\'m*)\n" +
            "He *is* (He*\'s*)\n" +
            "She *is* (She*'s*)\n" +
            "It *is* (It*'s*)\n" +
            "We *are* (We*\'re*)\n" +
            "You *are* (You*'re*)\n" +
            "They *are* (They*'re*)\n\n" +
            "@negative@\n" +
            "I *am not* (I*'m not*)\n" +
            "He *is not* (He*'s not* _or_ He *isn't*)\n" +
            "She *is not* (She*'s not* _or_ She *isn't*)\n" +
            "It *is not* (It*'s not* _or_ It *isn't*)\n" +
            "We *are not* (We*'re not* _or_ We *aren't*)\n" +
            "You *are not* (You*'re* _or_ You *aren't*)\n" +
            "They *are not* (They*'re* _or_ They *aren't*)\n\n" +
            "\t\tI*'m* cold. Can you close the window, pleaser?\n" +
            "\t\tI*'m* 32 years old. My sister *is* 29.\n\n\n" +
            "That*'s* = That *is*\n" +
            "There*'s* = There *is*\n" +
            "Here*'s* = Here *is*"
)