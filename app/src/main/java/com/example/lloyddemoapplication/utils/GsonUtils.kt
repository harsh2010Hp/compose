package com.example.lloyddemoapplication.utils

import com.google.gson.Gson
import org.json.JSONObject


inline fun <reified T> String.fromJson(): T {
    return Gson().fromJson(this, T::class.java)
}
inline fun <reified T> T.toJson(): String {
    return Gson().toJson(this, T::class.java)
}