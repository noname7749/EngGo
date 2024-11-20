package com.example.enggo.ui.lesson

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.enggo.R
import com.example.enggo.model.course.Theory
import com.example.enggo.ui.theme.EngGoTheme
import com.example.enggo.ui.unit.UnitItemList
import com.example.enggo.ui.unit.UnitListTopAppBar

@Composable
internal fun LessonRoute(
    lessonId: Int?,
    onBackPress: () -> Unit,
) {
    // TODO()
    //val courseViewModel: CourseViewModel = viewModel(factory = CourseViewModel.Factory)
    LessonScreen(lessonId = lessonId)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonScreen(
    lessonId: Int?,
    modifier: Modifier = Modifier
) {
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)
    Scaffold(
        topBar = {
            LessonTopAppBar(lessonId = lessonId)
        },
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime),
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        TheoryScreen(modifier = Modifier.padding(paddingValues))
    }
}


@Composable
fun TheoryScreen(
    modifier: Modifier = Modifier
) {
    val sampleTheoryData = Theory(
        theoryId = 0,
        lessonId = 0,
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

    Column(
        modifier = Modifier
            .padding(
                vertical = dimensionResource(R.dimen.padding_small),
                horizontal = dimensionResource(R.dimen.padding_medium)
            ),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        Text(
            text = contentFormatter(sampleTheoryData.content),
            modifier = modifier
                //.fillMaxWidth()
                //.padding(dimensionResource(R.dimen.padding_medium))
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                //.fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_small))
        ) {
            Spacer(Modifier.weight(1f))
            Card(
                elevation = CardDefaults.cardElevation(),
                shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
                onClick = {  } // TODO
            ) {
                Text(
                    text = "Go to exercise",
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_medium)),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonTopAppBar(
    lessonId: Int?,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Lesson WITH ID $lessonId", // TODO
                    style = MaterialTheme.typography.displaySmall
                )
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
fun TheoryScreenPreview() {
    EngGoTheme {
        Surface() {
            TheoryScreen()
        }
    }
}

@Preview
@Composable
fun LessonScreenPreview() {
    EngGoTheme {
        Surface{
            LessonScreen(lessonId = 5)
        }
    }
}