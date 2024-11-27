package com.example.enggo.ui.flashcard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.enggo.model.Flashcard
import com.example.enggo.model.FlashcardFolder

class FlashcardFolderReviewViewModel : ViewModel() {
    private val _firstCard = MutableLiveData<String>()
    val firstCard : LiveData<String> = _firstCard

    private val _secondCard = MutableLiveData<String>()
    val secondCard : LiveData<String> = _secondCard

    fun changeReviewCard(s1 : String, s2 : String) {
        _firstCard.value = s1
        _secondCard.value = s2
    }

}