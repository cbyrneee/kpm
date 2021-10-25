package dev.cbyrne.kpm

import dev.cbyrne.kpm.cache.CacheManager
import dev.cbyrne.kpm.project.Project
import dev.cbyrne.kpm.scripting.manager.ScriptManager
import java.nio.file.Path
import kotlin.io.path.readText

class KPM(val currentProject: Project, val isCached: Boolean, private val cacheManager: CacheManager) {
    companion object {
        private val scripts = ScriptManager()

        fun load(script: Path, directory: Path): KPM {
            val cacheManager = CacheManager(directory)
            val project = cacheManager.readProjectFromCache() ?: parseScript(script)

            return KPM(project, cacheManager.cacheExists(), cacheManager)
        }

        fun parseScript(script: Path) = Project(scripts.parse(script.readText()))
    }

    fun generateCache() {
        if (!isCached) cacheManager.generateCache(currentProject)
    }
}