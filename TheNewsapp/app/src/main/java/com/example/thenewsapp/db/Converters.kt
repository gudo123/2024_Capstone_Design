package com.example.thenewsapp.db

import androidx.room.TypeConverter
import com.example.thenewsapp.models.Source
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromSource(source: Source?): String? {
        return Gson().toJson(source)
    }

    @TypeConverter
    fun toSource(sourceString: String?): Source? {
        return Gson().fromJson(sourceString, object : TypeToken<Source>() {}.type)
    }
}
