package dev.cbyrne.kpm.extension

import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.exists

fun Path.createDirectoryIfNotExists() {
    if (!exists()) createDirectories()
}

fun Path.createFileIfNotExists() {
    if (!exists()) createFile()
}