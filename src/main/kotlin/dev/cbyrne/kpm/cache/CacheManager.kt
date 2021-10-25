package dev.cbyrne.kpm.cache

import dev.cbyrne.kpm.extension.createDirectoryIfNotExists
import dev.cbyrne.kpm.extension.createFileIfNotExists
import dev.cbyrne.kpm.project.Project
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.file.Path
import kotlin.io.path.div
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

class CacheManager(path: Path) {
    private val cachesDir = path / ".kpm"
    private val cachesFile = cachesDir / "kpm.cache.json"

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        isLenient = true
    }

    fun cacheExists() = cachesFile.exists()

    fun generateCache(project: Project) {
        createCacheFiles()
        cachesFile.writeText(json.encodeToString(project))
    }

    fun readProjectFromCache(): Project? {
        return if (cacheExists()) json.decodeFromString<Project>(cachesFile.readText()) else null
    }

    private fun createCacheFiles() {
        cachesDir.createDirectoryIfNotExists()
        cachesFile.createFileIfNotExists()
    }
}