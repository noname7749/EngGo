package com.example.enggo.data.dictionary.converters

import androidx.room.TypeConverter
import com.example.enggo.model.dictionary.Meaning
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MeaningConverter {
    @TypeConverter
    fun fromJson(meaning: List<Meaning>): String {
        return Gson().toJson(meaning)
    }

    @TypeConverter
    fun fromJson(jsonString: String): List<Meaning> {
        return Gson().fromJson(jsonString, object: TypeToken<List<Meaning>>() {}.type)
            ?: emptyList()
    }
}