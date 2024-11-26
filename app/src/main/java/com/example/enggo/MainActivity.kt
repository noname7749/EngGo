package com.example.enggo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.enggo.model.Flashcard
import com.example.enggo.model.FlashcardFolder
import com.example.enggo.ui.flashcard.FlashcardFolderView
import com.example.enggo.ui.navigation.App
import com.example.enggo.ui.navigation.rememberAppState
import com.example.enggo.ui.theme.EngGoTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EngGoTheme {
                var t : FlashcardFolder = FlashcardFolder()
                t.name = "Testing flashcard folder"
                t.addFlashcard(Flashcard("Vehicle", "Thuộc về xe cộ (adj)"))
                t.addFlashcard(Flashcard("vinicity", "Vùng lân cận, phụ cận"))
                t.addFlashcard(Flashcard("cater", "Phục vụ, cung cấp (v)"))
                t.addFlashcard(Flashcard("Pastoral Care", "Mục vụ (trách nhiệm chăm sóc và tư vấn được cung cấp bởi các nhà lãnh đạo tôn giáo)"))
                Surface(modifier = Modifier.fillMaxSize()) {
                    FlashcardFolderView(fcFolder = t)
                }
            }
        }
        FirebaseApp.initializeApp(this)
    }
}

