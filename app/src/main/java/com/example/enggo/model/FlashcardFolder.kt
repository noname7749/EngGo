package com.example.enggo.model

class FlashcardFolder (
    var name : String = "Folder name",
    var userId : String? = ""
) {
    var flashcardNumber : Int = 0
    val flashcardList : MutableList<Flashcard> = mutableListOf()

    fun addFlashcard(x : Flashcard) {
        flashcardList.add(x)
        flashcardNumber++
    }

}

