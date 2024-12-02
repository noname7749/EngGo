package com.example.enggo.ui.home

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.enggo.R
import com.example.enggo.data.service.CourseService
import com.example.enggo.data.service.UserService
import com.example.enggo.model.RecentCourse
import com.example.enggo.model.course.Course
import com.example.enggo.ui.course.CourseListImageItem
import com.example.enggo.ui.course.CourseViewModel
import com.example.enggo.ui.course.CourseViewModelFactory
import com.example.enggo.ui.course.LevelTitles
import com.example.enggo.ui.profile.getCurrentProfileData
import com.google.firebase.firestore.FirebaseFirestore

@Composable
internal fun HomeRoute(
    onRecentCourseClick: (Int, String) -> Unit,
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
        onRecentCourseClick = onItemClick
    )
}

@Composable
fun HomeScreen(
    recentCoursesList: List<RecentCourse>,
    onRecentCourseClick: (Int, String) -> Unit,
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
        }
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
    Log.d("COURSE IN HOME", course.course_name.toString())
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