package com.example.enggo.ui.course

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastCbrt
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.enggo.R
import com.example.enggo.model.course.Course
import com.example.enggo.model.course.Lesson
import com.example.enggo.model.course.UnitData
import com.example.enggo.ui.theme.EngGoTheme

@Composable
internal fun CoursesRoute() {
    // TODO()
    val courseViewModel: CourseViewModel = viewModel(factory = CourseViewModel.Factory)
    CoursesScreen(
        //courseUiState = courseViewModel.courseUiState
    )
}

@Composable
fun CoursesScreen(
    //courseUiState: CourseUiState,
) {
    // TODO()

//    when (courseUiState) {
//        is CourseUiState.Loading -> Text(text = "loading")
//        is CourseUiState.Success -> Text(text = courseUiState.courses[0].courseName) // TODO()
//        is CourseUiState.Error -> Text(text = "error")
//    }

    val coursesList = listOf(
        Course(courseId = 1, courseName = "Kotlin Programming", description = "Learn Kotlin from beginner to advanced level.", level = 1),
        Course(courseId = 2, courseName = "Android Development", description = "Build Android apps using Kotlin.", level = 2),
        Course(courseId = 3, courseName = "Machine Learning", description = "Introduction to Machine Learning concepts.", level = 3)
    )
    Scaffold(
        topBar = {
            CoursesTopAppBar()
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)
            .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Temp Elementary Courses",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium)),
            )
            CourseListRow(
                coursesList = coursesList,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Temp Pre-intermediate Courses",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium)),
            )
            CourseListRow(
                coursesList = coursesList,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Temp Intermediate Courses",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium)),
            )
            CourseListRow(
                coursesList = coursesList,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Temp Advanced Courses",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium)),
            )
            CourseListRow(
                coursesList = coursesList,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Temp IELTS Courses",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium)),
            )
            CourseListRow(
                coursesList = coursesList,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
//        LazyColumn(contentPadding = it) {
//
//        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesTopAppBar(modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.courses),
                    style = MaterialTheme.typography.displaySmall
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun CourseListRow(
    coursesList: List<Course>,
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
        items(coursesList, key = { course -> course.courseId }) { course ->
            CourseListItem(
                courses = course,
                onItemClick = {
                    // TODO: click
                },
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
fun CourseListItem(
    courses: Course,
    onItemClick: (Course) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(),
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        onClick = { onItemClick(courses) }
    ) {
        Row (
            modifier = Modifier.fillMaxWidth()
                .size(128.dp) // TODO: create dimens value
        ){
            CourseListImageItem(
                courseId = courses.courseId,
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
                    text = courses.courseName,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 4.dp) // TODO: create dimens value
                )
                Text(
                    text = courses.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3
                )
                Spacer(Modifier.weight(1f))
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (courses.level == 1) "Elementary" else "Intermediate", // TODO
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

@Composable
fun CourseListImageItem(
    courseId: Int,
    modifier: Modifier = Modifier
) {
    val courseImages = listOf(
        R.drawable.course_1,
        R.drawable.course_2,
        R.drawable.course_3,
        R.drawable.course_4,
        R.drawable.course_5,
        R.drawable.course_6,
        R.drawable.course_7,
        R.drawable.course_8,
        R.drawable.course_9,
        R.drawable.course_10
    )

    val imageIndex = courseId % courseImages.size // TODO() set something for fun
    val randomImage = courseImages[imageIndex]

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = randomImage),
            contentDescription = null,
            alignment = Alignment.Center,
            contentScale = ContentScale.Fit,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
                .align(Alignment.Center)
        )
    }
}

@Composable
fun UnitItemList(
    unitData: UnitData,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        elevation = CardDefaults.cardElevation(),
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        onClick = { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier
                .padding(
                    vertical = dimensionResource(R.dimen.padding_small),
                    horizontal = dimensionResource(R.dimen.padding_medium)
                )
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_small))
            ) {
                Text(
                    text = unitData.unitName,
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(Modifier.weight(1f))
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
            if (expanded) {
                HorizontalDivider(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_small)))
                LessonList(
                    lessonList = unitData.lesson,
                    onItemClick = {}, // TODO
                    modifier = Modifier.padding(
                        start = dimensionResource(R.dimen.padding_medium),
                        top = dimensionResource(R.dimen.padding_small),
                        bottom = dimensionResource(R.dimen.padding_medium),
                        end = dimensionResource(R.dimen.padding_medium)
                    )
                )
            }
        }
    }
}

@Composable
fun LessonList(
    lessonList: List<Lesson>,
    onItemClick: (Lesson) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        itemsIndexed(lessonList) { index, lesson ->
            LessonItemList(
                lesson = lesson,
                onItemClick = { onItemClick(lesson) }
            )
            if (index != lessonList.size - 1) {
                HorizontalDivider(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_small)))
            }
        }
    }
}

@Composable
fun LessonItemList(
    lesson: Lesson,
    onItemClick: (Lesson) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(),
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        onClick = { onItemClick(lesson) },
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(
                vertical = dimensionResource(R.dimen.padding_small),
                horizontal = dimensionResource(R.dimen.padding_medium)
            )
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .size(64.dp) // TODO
                    .padding(dimensionResource(R.dimen.padding_small))
            ) {
                Text(
                    text = lesson.lessonName,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 4.dp) // TODO: create dimens value
                )
                Spacer(Modifier.weight(1f))
                val text = when {
                    lesson.hasTheory && lesson.hasExercise -> "Theory | Exercise"
                    lesson.hasTheory -> "Theory"
                    else -> "Exercise"
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            //Spacer(Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Preview
@Composable
fun CoursesRowPreview() {
    val coursesList = listOf(
        Course(courseId = 1, courseName = "Kotlin Programming", description = "Learn Kotlin from beginner to advanced level.", level = 1),
        Course(courseId = 2, courseName = "Android Development", description = "Build Android apps using Kotlin.", level = 2),
        Course(courseId = 3, courseName = "Machine Learning", description = "Introduction to Machine Learning concepts.", level = 3)
    )
    EngGoTheme {
        Surface {
            CourseListRow(
                coursesList = coursesList,
            )
        }
    }

}

@Preview
@Composable
fun CoursesListItemPreview() {
    val sampleCourse = Course(
        courseId = 1,
        courseName = "Kotlin Programming",
        description = "Learn Kotlin from beginner to advanced level.",
        level = 1
    )
    EngGoTheme {
        Surface {
            CourseListItem(
                courses = sampleCourse,
                onItemClick = {},
            )
        }
    }
}

@Preview
@Composable
fun UnitItemListPreview() {
    val sampleUnitData = UnitData(
        unitId = 1,
        unitName = "Music",
        lesson = listOf(
            Lesson(lessonId = 1, hasTheory = true, hasExercise = true, lessonName = "Grammar"),
            Lesson(lessonId = 2, hasTheory = true, hasExercise = false, lessonName = "Listening"),
            Lesson(lessonId = 3, hasTheory = false, hasExercise = true, lessonName = "Reading")
        )
    )
    EngGoTheme {
        Surface {
            UnitItemList(
                unitData = sampleUnitData
            )
        }
    }
}

@Preview
@Composable
fun LessonItemListPreview() {
    val sampleLesson = Lesson(
        lessonId = 1,
        hasTheory = true,
        hasExercise = true,
        lessonName = "Grammar - Present simple"
    )
    EngGoTheme {
        Surface {
            LessonItemList(
                lesson = sampleLesson,
                onItemClick = {}
            )
        }
    }
}

@Preview
@Composable
fun CourseScreenPreview() {
    EngGoTheme {
        Surface {
            CoursesScreen()
        }
    }
}