package com.example.enggo.ui.dictionary.data.converters

import androidx.room.TypeConverter
import com.example.enggo.ui.dictionary.model.Label
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LabelConverter {
    @TypeConverter
    fun fromList(labels: List<Label>): String {
        return Gson().toJson(labels)
    }

    @TypeConverter
    fun fromJson(jsonString: String): List<Label> {
        return Gson().fromJson(jsonString, object: TypeToken<List<Label>>() {}.type)
    }
}