package com.example.enggo.ui.flashcard

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.enggo.R
import com.example.enggo.model.FlashcardFolder
import com.example.enggo.model.termCreate
import com.example.enggo.ui.theme.EngGoTheme

@Composable
fun createFCFolderScreen(fcFolder : FlashcardFolder) {
    var terms = remember { mutableStateListOf<String>() }
    var defs = remember { mutableStateListOf<String>() }
    var removeIndexAt by remember { mutableStateOf(0) }
    var FolderName by remember { mutableStateOf("Folder name") }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (terms.size < 2) {
            terms.add("")
            terms.add("")
            defs.add("")
            defs.add("")
        }
        Text(
            text = "Top bar",
            fontSize = 20.sp,
            modifier = Modifier.height(50.dp)
        )

        TextField(
            value = FolderName,
            onValueChange = { new_foldername ->
                FolderName = new_foldername
            },
            modifier = Modifier.fillMaxWidth()
                .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
        )

        Text(
            text = "Title",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 10.dp, end = 10.dp)
        )

        Row() {
            Card(
                modifier = Modifier.fillMaxWidth(0.5f)
                    .padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
                    .border(1.dp, Color.Black)
                    .clickable {
                        terms.add("")
                        defs.add("")
                    }
            ) {
                Text(
                    text = "Add Term",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 10.dp, top = 5.dp, bottom = 5.dp)
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp, end = 10.dp)
                    .border(1.dp, Color.Black)
                    .clickable {
                        //Done
                    }
            ) {
                Text(
                    text = "Finish",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 10.dp, top = 5.dp, bottom = 5.dp)
                )
            }
        }
        LazyColumn(
            modifier = Modifier.padding(bottom = 50.dp)
        ) {
            itemsIndexed(terms) { index, item ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                ) {
                    Column() {
                        Row(modifier = Modifier.height(30.dp)) {
                            Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(0.9f))
                            if (terms.size >= 3) {
                                Box(
                                    modifier = Modifier.fillMaxSize()
                                        .clickable {
                                            removeIndexAt = index
                                            terms.removeAt(index)
                                            defs.removeAt(index)
                                        }
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.deletebutton),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                        TextField(
                            value = terms[index],
                            onValueChange = { terms[index] = it },
                            modifier = Modifier.fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
                        )

                        Text(
                            text = "TERM",
                            fontSize = 15.sp,
                            modifier = Modifier.padding(start = 10.dp)
                        )

                        TextField(
                            value = defs[index],
                            onValueChange = { defs[index] = it },
                            modifier = Modifier.fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
                        )

                        Text(
                            text = "DEFINITION",
                            fontSize = 15.sp,
                            modifier = Modifier.padding(start = 10.dp, bottom = 5.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun createFCFolderPreview() {
    EngGoTheme {
        var t : FlashcardFolder = FlashcardFolder("ABC")
        Surface(modifier = Modifier.fillMaxSize()) {
            createFCFolderScreen(fcFolder = t)
        }
    }
}