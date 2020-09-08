package com.example.main.kotlin.br.com.authKtor.extension

fun <T, R> T?.optional(caseSome: (T) -> R, caseNone: () -> R): R =
    if (this == null) caseNone() else caseSome(this)
