package com.example.enggo.ui.flashcard

import android.content.Context
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
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
import androidx.compose.ui.platform.LocalContext
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
    var id2 by remember { mutableStateOf(0) }

    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("EngGoApp", Context.MODE_PRIVATE)
    val idUser: String? = sharedPref.getString("currentUserId", null)

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

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        modifier = Modifier.size(50.dp)
                            .padding(end = 16.dp),
                        onClick = {
                            flashcardNumber++
                            firstCard.add("")
                            secondCard.add("")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add"
                        )
                    }

                    IconButton(
                        modifier = Modifier.size(50.dp)
                            .padding(end = 16.dp),
                        onClick = {
                            if (name.equals("") == false) {
                                var f = FlashcardFolder(name = name, userId = idUser)
                                folderCollectionRef
                                    .get()
                                    .addOnSuccessListener { document ->
                                        id2 = document.count() + 1
                                        folderCollectionRef.document(id2.toString()).set(f)
                                            .addOnSuccessListener {
                                                folderCollectionRef.document(id).update("userId", "NULL")
                                            }
                                        for (i in 0..firstCard.size - 1) {
                                            if (firstCard[i].equals("") or secondCard[i].equals(""))
                                                continue
                                            var fc = Flashcard(firstCard[i], secondCard[i], id2.toString())
                                            fcCollectionRef.add(fc)
                                                .addOnSuccessListener {
                                                    navController.navigate("FlashcardReview/$id2")
                                                }
                                        }
                                    }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Finish"
                        )
                    }
                }
            }

            TextField(
                value = name,
                onValueChange = { new_foldername ->
                    name = new_foldername
                },
                placeholder = { Text("Folder name") },
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
            )

            if (name == "") {
                Text(
                    text = "Enter folder name",
                    fontSize = 15.sp,
                    color = Color.Red,
                    modifier = Modifier.padding(start = 10.dp, bottom = 10.dp)
                )
            }
            else {
                Text(
                    text = "",
                    fontSize = 15.sp,
                    modifier = Modifier.height(10.dp).padding(bottom = 10.dp)
                )
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

