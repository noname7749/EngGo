package com.example.enggo.ui.flashcard

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.enggo.R
import com.example.enggo.model.Flashcard
import com.example.enggo.model.FlashcardFolder
import com.example.enggo.ui.flashcard.navigation.navigateToFlashcard
import com.example.enggo.ui.theme.EngGoTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

private val fcCollectionRef = Firebase.firestore.collection("Flashcard")
private val folderCollectionRef = Firebase.firestore.collection("Folder")

@Composable
fun editFCFolderScreen(
    id : String,
    navController: NavController
) {
    var ok by remember { mutableStateOf(0) }
    var tChanged by remember { mutableStateOf("") }
    var dChanged by remember{ mutableStateOf("") }
    var tIndex by remember{ mutableStateOf(-1) }
    var dIndex by remember{ mutableStateOf(-1) }
    var removeIndexAt by remember { mutableStateOf(0) }
    var init by remember { mutableStateOf(1) }
    var flashcardNumber by remember { mutableStateOf(0) }
    var firstCard = remember { mutableListOf<String>() }
    var secondCard = remember { mutableListOf<String>() }
    var name by remember { mutableStateOf("Folder Name") }

    if (init == 1) {
        fcCollectionRef.whereEqualTo("folderid", id)
            .get()
            .addOnSuccessListener { documents->
                flashcardNumber = documents.count()
                for (document in documents) {
                    firstCard.add(document.get("firstCard").toString())
                    secondCard.add(document.get("secondCard").toString())
                }
            }

        folderCollectionRef.document(id)
            .get()
            .addOnSuccessListener { document->
                name = document.get("name").toString()
            }

        init = 0
    }

    if (tIndex != -1) {
        firstCard[tIndex] = tChanged
        tIndex = -1
    }

    if (dIndex != -1) {
        secondCard[dIndex] = dChanged
        dIndex = -1
    }

    if (init == 0) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    modifier = Modifier.size(50.dp)
                        .padding(start = 16.dp, top = 10.dp),
                    onClick = {
                        navController.navigate("FlashcardReview/$id")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }

            TextField(
                value = name,
                onValueChange = { new_foldername ->
                    name = new_foldername
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
                            flashcardNumber++
                            firstCard.add("")
                            secondCard.add("")
                        }
                ) {
                    Text(
                        text = "Add Term",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            start = 10.dp,
                            top = 5.dp,
                            bottom = 5.dp,
                            end = 10.dp
                        )
                    )
                }

                Card(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp, end = 10.dp)
                        .border(1.dp, Color.Black)
                        .clickable {
                            if (ok == 0) {
                                fcCollectionRef.whereEqualTo("folderid", id)
                                    .get()
                                    .addOnSuccessListener { documents ->
                                        for (document in documents) {
                                            fcCollectionRef.document(document.id).delete()
                                        }
                                        ok = 1
                                    }
                            }

                            if (ok == 1) {
                                folderCollectionRef.document(id).update("name", name)
                                for (i in 0..firstCard.size - 1) {
                                    if (firstCard[i].equals("") or secondCard[i].equals(""))
                                        continue
                                    Log.d("Firebase", "${firstCard[i]} ${secondCard[i]} $id")
                                    var fc = Flashcard(
                                        FirstCard = firstCard[i],
                                        SecondCard = secondCard[i],
                                        folderid = id
                                    )
                                    fcCollectionRef.add(fc)
                                }
                                ok = 2
                            }
                            if (ok == 2) {
                                navController.navigate("FlashcardReview/$id")
                            }
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
            if (firstCard.size >= flashcardNumber) {
                LazyColumn(
                    modifier = Modifier.padding(bottom = 50.dp)
                ) {
                    itemsIndexed(firstCard) { index, item ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                        ) {
                            Column() {
                                Row(modifier = Modifier.height(30.dp)) {
                                    Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(0.9f))
                                    if (firstCard.size >= 3) {
                                        Box(
                                            modifier = Modifier.fillMaxSize()
                                                .clickable {
                                                    removeIndexAt = index
                                                    flashcardNumber--
                                                    firstCard.removeAt(removeIndexAt)
                                                    secondCard.removeAt(removeIndexAt)
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
                                    value = firstCard[index],
                                    onValueChange = {
                                        tIndex = index
                                        tChanged = it
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                        .padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
                                )

                                Text(
                                    text = "TERM",
                                    fontSize = 15.sp,
                                    modifier = Modifier.padding(start = 10.dp)
                                )

                                TextField(
                                    value = secondCard[index],
                                    onValueChange = {
                                        dIndex = index
                                        dChanged = it
                                    },
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
    }
}

@Preview(showBackground = true)
@Composable
fun editFCFolderPreview() {
    EngGoTheme {

    }
}

