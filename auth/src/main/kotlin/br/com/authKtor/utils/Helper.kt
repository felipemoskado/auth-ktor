package com.example.main.kotlin.br.com.authKtor.utils

import com.google.gson.Gson

val gson: Gson = Gson()

fun <T> T.toJson(): String = gson.toJson(this)
inline fun <reified T> String.fromObject(): T = gson.fromJson(this, T::class.java)