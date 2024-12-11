
package com.example.enggo.ui.flashcard

import android.content.Context
import android.util.Log
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
import com.example.enggo.model.termCreate
import com.example.enggo.ui.flashcard.navigation.navigateToFlashcard
import com.example.enggo.ui.theme.EngGoTheme
import com.google.firebase.*
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Delay
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*


private val fcCollectionRef = Firebase.firestore.collection("Flashcard")
private val folderCollectionRef = Firebase.firestore.collection("Folder")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun createFCFolderScreen(
    navController: NavController
) {
    var terms = remember { mutableStateListOf<String>() }
    var defs = remember { mutableStateListOf<String>() }
    var folderName by remember { mutableStateOf("Folder name") }
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("EngGoApp", android.content.Context.MODE_PRIVATE)
    val idUser: String? = sharedPref.getString("currentUserId", null)

    if (terms.isEmpty()) {
        terms.add("")
        terms.add("")
        defs.add("")
        defs.add("")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val folder = FlashcardFolder(name = folderName, userId = idUser)
                            folderCollectionRef.add(folder)
                                .addOnSuccessListener { docRef ->
                                    val folderId = docRef.id
                                    for (i in terms.indices) {
                                        if (terms[i].isEmpty() || defs[i].isEmpty())
                                            continue
                                        val flashcard = Flashcard(
                                            FirstCard = terms[i],
                                            SecondCard = defs[i],
                                            folderid = folderId
                                        )
                                        fcCollectionRef.add(flashcard)
                                    }
                                    navController.navigateUp()
                                }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Save"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                terms.add("")
                defs.add("")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Term")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            item {
                TextField(
                    value = folderName,
                    onValueChange = { folderName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    label = { Text("Title") }
                )
            }

            itemsIndexed(terms) { index, _ ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        if (terms.size > 2) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(onClick = {
                                    terms.removeAt(index)
                                    defs.removeAt(index)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Delete"
                                    )
                                }
                            }
                        }

                        TextField(
                            value = terms[index],
                            onValueChange = { terms[index] = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("TERM") }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        TextField(
                            value = defs[index],
                            onValueChange = { defs[index] = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("DEFINITION") }
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