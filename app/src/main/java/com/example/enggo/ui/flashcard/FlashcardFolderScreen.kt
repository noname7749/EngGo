package com.example.enggo.ui.flashcard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.enggo.ui.theme.EngGoTheme

@Composable
fun FlashcardHomeScreen(modifier: Modifier = Modifier) {

    var filterText by remember { mutableStateOf("Enter Filter") }

    Column(modifier = modifier) {
        Text(
            text = "Folder",
            fontSize = 40.sp,
            modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 16.dp)
        )

        TextField(
            value = filterText,
            onValueChange = { filterText = it },
            label = { Text("Filter") },
            modifier = Modifier.fillMaxWidth()
                .padding(start = 16.dp, bottom = 40.dp, end = 10.dp)
        )

        FolderList()

    }
}


@Composable
fun FolderList() {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            FLashcardFolder(name = "Flashcard 1", numOfItems = 10)
            FLashcardFolder(name = "Flashcard 2", numOfItems = 12)
        }
    }
}


@Composable
fun FLashcardFolder(name: String, numOfItems: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, bottom = 30.dp, end = 16.dp)
    ) {
        Column {
            Text(
                text = name,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 5.dp, top = 5.dp, bottom = 60.dp)
            )

            Text(
                text = "$numOfItems items",
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 5.dp, bottom = 5.dp)
            )
        }
    }
}

@Preview (showBackground = true)
@Composable
fun LibraryScreenReview() {
    EngGoTheme {
        FlashcardHomeScreen(Modifier.fillMaxSize())
    }
}

