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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.enggo.R
import com.example.enggo.model.Flashcard
import com.example.enggo.model.FlashcardFolder
import com.example.enggo.model.termCreate
import com.example.enggo.ui.flashcard.navigation.navigateToFlashcard
import com.example.enggo.ui.theme.EngGoTheme
import com.google.firebase.*
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Delay

private val fcCollectionRef = Firebase.firestore.collection("Flashcard")
private val folderCollectionRef = Firebase.firestore.collection("Folder")

@Composable
fun createFCFolderScreen(
    navController: NavController
) {
    var terms = remember { mutableStateListOf<String>() }
    var defs = remember { mutableStateListOf<String>() }
    var id by remember { mutableStateOf(0) }
    var removeIndexAt by remember { mutableStateOf(0) }
    var FolderName by remember { mutableStateOf("") }
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("EngGoApp", Context.MODE_PRIVATE)
    val idUser: String? = sharedPref.getString("currentUserId", null)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (terms.size < 2) {
            terms.add("")
            terms.add("")
            defs.add("")
            defs.add("")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 10.dp, end = 16.dp)
        ) {
            IconButton(
                modifier = Modifier.size(50.dp),
                onClick = { navController.navigateToFlashcard() }
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
                        terms.add("")
                        defs.add("")
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
                        if (FolderName.equals("") == false) {
                            var f = FlashcardFolder(name = FolderName, userId = idUser)
                            folderCollectionRef
                                .get()
                                .addOnSuccessListener { document ->
                                    id = document.count() + 1
                                    //Log.d("Test count 1", id.toString())
                                    folderCollectionRef.document(id.toString()).set(f)
                                    for (i in 0..terms.size - 1) {
                                        if (terms[i].equals("") or defs[i].equals(""))
                                            continue
                                        var fc = Flashcard(terms[i], defs[i], id.toString())
                                        fcCollectionRef.add(fc)
                                            .addOnSuccessListener {
                                                navController.navigate("Flashcard")
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
            value = FolderName,
            onValueChange = { new_foldername ->
                FolderName = new_foldername
            },
            placeholder = { Text("Folder name") },
            modifier = Modifier.fillMaxWidth()
                .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
        )

        if (FolderName == "") {
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
                                IconButton(
                                    onClick = {
                                        removeIndexAt = index
                                        terms.removeAt(index)
                                        defs.removeAt(index)
                                    },
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete"
                                    )
                                }
                            }
                        }
                        TextField(
                            value = terms[index],
                            onValueChange = {
                                terms[index] = it
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
                            value = defs[index],
                            onValueChange = {
                                defs[index] = it
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

@Preview(showBackground = true)
@Composable
fun createFCFolderPreview() {
    EngGoTheme {
        Surface(modifier = Modifier.fillMaxSize()) {

        }
    }
}