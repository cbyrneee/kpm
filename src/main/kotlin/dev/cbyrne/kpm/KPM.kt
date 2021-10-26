package dev.cbyrne.kpm

import dev.cbyrne.kpm.compile.BuildManager
import dev.cbyrne.kpm.dependency.DependencyManager
import dev.cbyrne.kpm.file.KPMFileManager
import dev.cbyrne.kpm.project.Project
import dev.cbyrne.kpm.scripting.manager.ScriptManager
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.nio.file.Path
import kotlin.io.path.readText

class KPM(val project: Project, val fileManager: KPMFileManager) {

    private val buildManager = BuildManager(this)
    private val dependencyManager = DependencyManager(this)

    fun initialize() {
        dependencyManager.resolveDependencies()
    }

    fun build() {
        buildManager.compile()
    }

    companion object {
        val scriptManager = ScriptManager()
        val logger: Logger = LogManager.getLogger("kpm")

        fun load(script: Path, directory: Path) =
            KPM(
                Project.parse(script.readText()),
                KPMFileManager(directory)
            )
    }
}