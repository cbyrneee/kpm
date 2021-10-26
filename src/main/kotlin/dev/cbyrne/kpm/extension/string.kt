package dev.cbyrne.kpm.extension

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val json = Json {
    isLenient = true
    prettyPrint = true
    ignoreUnknownKeys = true
}

inline fun <reified T> String.decode(): T = json.decodeFromString(this)

fun Any.encode(): String = json.encodeToString(this)
