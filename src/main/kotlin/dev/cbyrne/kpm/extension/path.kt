package dev.cbyrne.kpm.extension

import dev.cbyrne.kpm.file.KPMFileManager
import java.io.FileOutputStream
import java.nio.file.Path
import java.util.zip.ZipOutputStream
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.relativeTo

fun Path.createDirectoryIfNotExists() {
    if (!exists()) createDirectories()
}

fun Path.createFileIfNotExists(): Path {
    if (!exists()) createFile()
    return this
}

fun Path.createFileAndParentIfNotExists(): Path {
    parent.createDirectoryIfNotExists()
    createFileIfNotExists()

    return this
}

fun Path.deleteRecursively() = toFile().deleteRecursively()

fun Path.relativeToRootString(fileManager: KPMFileManager) = "${this.relativeTo(fileManager.rootDir)}"

fun Path.zipOutputStream() = ZipOutputStream(FileOutputStream(toFile()))
