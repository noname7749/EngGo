package com.example.enggo.ui.unit

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.enggo.R
import com.example.enggo.model.course.Lesson
import com.example.enggo.model.course.UnitData
import com.example.enggo.ui.theme.EngGoTheme

@Composable
internal fun UnitListRoute(
    courseId: Int?,
    onBackPress: () -> Unit,
) {
    Text(
        text = "YOU JUST CLICK COURSE WITH ID $courseId"
    )
    // TODO()
    //val courseViewModel: CourseViewModel = viewModel(factory = CourseViewModel.Factory)
    UnitListScreen()
}

@Composable
fun UnitListScreen() {
//    Text(
//        text = "Unit List Screen"
//    )
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