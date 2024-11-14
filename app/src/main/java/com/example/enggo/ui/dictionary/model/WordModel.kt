package com.example.enggo.ui.dictionary.model

data class WordModel(
    val meanings: List<Meaning>?,
    val word: String,
    val wordsetId: String
)
