package dev.cbyrne.kpm

import com.squareup.tools.maven.resolution.FetchStatus
import dev.cbyrne.kpm.compile.BuildManager
import dev.cbyrne.kpm.dependency.DependencyManager
import dev.cbyrne.kpm.extension.relativeToRootString
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
        val dependencies = dependencyManager
            .resolve()
            .getOrElse {
                return logger.warn("Failed to resolve dependencies: ${it.localizedMessage}")
            }
            .mapNotNull { (dependency, result) ->
                when (result.status) {
                    is FetchStatus.RepositoryFetchStatus.SUCCESSFUL -> result.artifact
                        ?: error("Failed to cast artifact of $result.")
                    is FetchStatus.RepositoryFetchStatus.NOT_FOUND -> {
                        logger.error("Unable to resolved dependency $dependency, it is not present in any of the repositories available.")
                        null
                    }
                    else -> {
                        logger.error("Unable to resolve $dependency.")
                        null
                    }
                }
            }
            .mapNotNull {
                when (dependencyManager.download(it)) {
                    is FetchStatus.RepositoryFetchStatus.SUCCESSFUL.SUCCESSFULLY_FETCHED -> it
                    is FetchStatus.RepositoryFetchStatus.SUCCESSFUL.FOUND_IN_CACHE -> it
                    is FetchStatus.RepositoryFetchStatus.NOT_FOUND -> {
                        logger.error("Unable to resolved dependency ${it.coordinate}, it is not present in any of the repositories available.")
                        null
                    }
                    else -> {
                        logger.error("Unable to resolve ${it.coordinate}.")
                        null
                    }
                }
            }

        logger.info("Resolved ${dependencies.size} dependencies! ${dependencies.map { it.main.localFile }}")
    }

    fun build() =
        buildManager
            .compile()
            .getOrElse { return logger.error("Failed to compile. ${it.localizedMessage}.") }
            .let { logger.info("Build successful! (./${it.relativeToRootString(fileManager)})") }

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