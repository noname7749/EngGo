package com.example.enggo.ui.flashcard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.enggo.ui.flashcard.FlashcardHomeScreen

const val Flashcard_ROUTE = "Flashcard"

fun NavController.navigateToFlashcard(navOptions: NavOptions? = null) {
    navigate(Flashcard_ROUTE, navOptions)
}

fun NavGraphBuilder.flashcardHome(
) {
    composable(Flashcard_ROUTE) {
        FlashcardHomeScreen()
    }
}