package com.example.enggo.ui.home

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.enggo.R
import com.example.enggo.api.ChatGPTClient
import com.example.enggo.data.service.CourseService
import com.example.enggo.data.service.UserService
import com.example.enggo.model.GPTQuestion
import com.example.enggo.model.RecentCourse
import com.example.enggo.model.course.Course
import com.example.enggo.ui.course.CourseListImageItem
import com.example.enggo.ui.course.CourseViewModel
import com.example.enggo.ui.course.CourseViewModelFactory
import com.example.enggo.ui.course.LevelTitles
import com.example.enggo.ui.lesson.ExerciseScreen
import com.example.enggo.ui.lesson.LessonTopAppBar
import com.example.enggo.ui.profile.getCurrentProfileData
import com.google.firebase.firestore.FirebaseFirestore
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
internal fun HomeRoute(
    onRecentCourseClick: (Int, String) -> Unit,
    onGenerateQuestionClick: () -> Unit
) {
    // TODO()
    val courseService = CourseService(FirebaseFirestore.getInstance())
    val userService = UserService(FirebaseFirestore.getInstance())

    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("EngGoApp", Context.MODE_PRIVATE)
    val currentUserId = sharedPref.getString("currentUserId", "") ?: ""
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(courseService, userService, currentUserId))

    val recentCourse by homeViewModel.recentCourses.collectAsState()
    LaunchedEffect(currentUserId) {
        if (currentUserId.isNotEmpty()) {
            homeViewModel.fetchRecentCourses(currentUserId)
        }
    }


    val onItemClick: (Int, String) -> Unit = { courseId, courseName ->
        onRecentCourseClick(courseId, courseName)

        currentUserId.let {
            val course = Course(course_id = courseId, course_name = courseName, course_description = "")
            homeViewModel.checkAndUpdateRecentCourses(it, course)
        }
    }

    Log.d("RECENT COURSE", recentCourse.toString())



    HomeScreen(
        recentCoursesList = recentCourse,
        onRecentCourseClick = onItemClick,
        onGenerateQuestionClick = onGenerateQuestionClick
    )
}

@Composable
fun HomeScreen(
    recentCoursesList: List<RecentCourse>,
    onRecentCourseClick: (Int, String) -> Unit,
    onGenerateQuestionClick: () -> Unit
) {
    // TODO()


    Scaffold(
        topBar = {
            HomeTopAppBar()
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)
            .verticalScroll(rememberScrollState())
        ) {
            // TODO
            if (recentCoursesList.isNotEmpty()) {
                Text(
                    text = "Recent Courses",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium)),
                )

                RecentCourseListRow(
                    recentCoursesList = recentCoursesList,
                    onItemClick = onRecentCourseClick
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Generate your own T/F/NG",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium)),
            )
            GenerateQuestionItem(
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium)),
                onClick = onGenerateQuestionClick
            )
        }
    }
}

@Composable
fun GenerateQuestionItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(),
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        onClick = { onClick() }
    ) {
        Row (
            modifier = Modifier.fillMaxWidth()
                .size(128.dp) // TODO: create dimens value
        ){
            Box(
                modifier = Modifier
                    .size(128.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.reading),
                    contentDescription = null,
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_medium))
                        .align(Alignment.Center)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(128.dp) // TODO: create dimens value
                    .padding(
                        top = dimensionResource(R.dimen.padding_medium),
                        end = dimensionResource(R.dimen.padding_medium),
                        bottom = dimensionResource(R.dimen.padding_medium),
                    )
            ) {
                Text(
                    text = "Generate your own T/F/NG",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 4.dp) // TODO: create dimens value
                )
                Text(
                    text = "Generate your own T/F/NG questions, using GPT",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
                Spacer(Modifier.weight(1f))
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateQuestionTopAppBar(modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Question Generator",
                    style = MaterialTheme.typography.displaySmall
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun GenerateQuestionRoute(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            GenerateQuestionTopAppBar()
        },
    ) { paddingValues ->
        GenerateQuestionScreen(
            modifier = Modifier.padding(paddingValues),
            onBackClick = onBackClick
        )
    }
}

@Composable
fun GenerateQuestionScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    var paragraph by remember { mutableStateOf("") }

    var questions by remember { mutableStateOf<List<GPTQuestion>>(emptyList()) }
    var userAnswers by remember { mutableStateOf<List<String>>(emptyList()) }

    var isLoading by remember { mutableStateOf(false) }

    var showResult by remember { mutableStateOf(false) }
    var correctAnswers by remember { mutableStateOf<List<String>>(emptyList()) }

    Column(
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_medium))
            .verticalScroll(rememberScrollState()),
    ) {
        OutlinedTextField(
            value = paragraph,
            onValueChange = { paragraph = it },
            label = { Text("Enter a paragraph") },
            modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp),
            placeholder = { Text("Type a paragraph here") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isLoading = true
                generateGPTQuestions(paragraph) { newQuestions ->
                    questions = newQuestions
                    userAnswers = List(newQuestions.size) { "" }
                    showResult = false
                    correctAnswers = List(newQuestions.size) { "" }
                    isLoading = false
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Generate Questions")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            questions.forEachIndexed { index, question ->
                Text(
                    text = question.text,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                OutlinedTextField(
                    value = userAnswers[index], // Bind to the respective answer
                    onValueChange = { newAnswer ->
                        userAnswers = userAnswers.toMutableList().apply { this[index] = newAnswer }
                    },
                    label = { Text("Your answer") },
                    modifier = Modifier.fillMaxWidth(),
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
                                tint = if (userAnswers[index].trim().equals(question.answer, ignoreCase = true)) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )

                if (showResult && userAnswers[index].trim() != question.answer) {
                    Text(
                        text = "Correct answer: ${question.answer}",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    correctAnswers = userAnswers.mapIndexed { index, answer ->
                        if (answer.trim().equals(questions[index].answer, ignoreCase = true)) {
                            "Correct"
                        } else {
                            "Incorrect"
                        }
                    }
                    showResult = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Check Answers")
            }

            if (showResult) {
                val correctCount = correctAnswers.count { it == "Correct" }
                Text(
                    text = "$correctCount out of ${questions.size} correct!",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Button(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth().padding(top = dimensionResource(R.dimen.padding_small))
            ) {
                Text(
                    text = "Go back to Home",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}


fun generateGPTQuestions(paragraph: String, onQuestionsReceived: (List<GPTQuestion>) -> Unit) {
    ChatGPTClient.generateQuestions(paragraph) { newQuestions ->
        onQuestionsReceived(newQuestions)
    }
}

@Composable
fun RecentCourseListRow(
    recentCoursesList: List<RecentCourse>,
    onItemClick: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth(),
        //.padding(horizontal = dimensionResource(R.dimen.padding_medium)),
        contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_medium)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        userScrollEnabled = true
    ) {
        items(recentCoursesList, key = { recentCourse -> recentCourse.course.course_id }) { recentCourse ->
            RecentCourseListItem(
                course = recentCourse.course,
                onItemClick = onItemClick,
                modifier = Modifier
                    .fillParentMaxWidth()
                    .height(128.dp) // TODO: create dimens value
            )
        }
    }


    // TODO: need check
    LaunchedEffect(listState.firstVisibleItemIndex) {
        val firstVisibleIndex = listState.firstVisibleItemIndex
        val visibleItemOffset = listState.layoutInfo.visibleItemsInfo.firstOrNull()?.offset
        if (visibleItemOffset != null && visibleItemOffset > 0) {
            listState.animateScrollToItem(firstVisibleIndex + 1)
        }
    }
}

@Composable
fun RecentCourseListItem(
    course: Course,
    onItemClick: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(),
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        onClick = { onItemClick(course.course_id, course.course_name) }
    ) {
        Row (
            modifier = Modifier.fillMaxWidth()
                .size(128.dp) // TODO: create dimens value
        ){
            CourseListImageItem(
                courseId = course.course_id,
                modifier = Modifier.size(128.dp) // TODO: create dimens value
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(128.dp) // TODO: create dimens value
                    .padding(
                        top = dimensionResource(R.dimen.padding_medium),
                        end = dimensionResource(R.dimen.padding_medium),
                        bottom = dimensionResource(R.dimen.padding_medium),
                    )
            ) {
                Text(
                    text = course.course_name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 4.dp) // TODO: create dimens value
                )
                course.course_description.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2
                    )
                }
                Spacer(Modifier.weight(1f))
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val levelTitle = LevelTitles.values().firstOrNull { it.level == course.course_level }?.getShortTitle() ?: "Unknown"
                    Text(
                        text = levelTitle,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.home),
                    style = MaterialTheme.typography.displaySmall
                )
            }
        },
        modifier = modifier
    )
}