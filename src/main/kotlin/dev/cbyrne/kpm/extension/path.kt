package dev.cbyrne.kpm.extension

import dev.cbyrne.kpm.file.KPMFileManager
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.relativeTo

fun Path.createDirectoryIfNotExists() {
    if (!exists()) createDirectories()
}

fun Path.relativeToRootString(fileManager: KPMFileManager) = "${this.relativeTo(fileManager.rootDir)}"
