package dev.cbyrne.kpm.extension

import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

fun ZipFile.entriesSequence() = entries().asSequence()

inline fun <reified T> ZipFile.inputStream(
    entry: ZipEntry,
    block: (InputStream) -> T
) = getInputStream(entry).use(block)

fun ZipOutputStream.addEntry(name: String, bytes: ByteArray) {
    addEntry(name)
    write(bytes)
}

fun ZipOutputStream.addEntry(name: String) = putNextEntry(ZipEntry(name))