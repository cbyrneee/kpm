package dev.cbyrne.kpm.extension

import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.readText

fun Path.createDirectoryIfNotExists() {
    if (!exists()) createDirectories()
}

fun Path.createFileIfNotExists() {
    if (!exists()) createFile()
}

inline fun <reified T> Path.decodeText(): T = readText().decode()