package dev.cbyrne.kpm

import com.squareup.tools.maven.resolution.FetchStatus
import dev.cbyrne.kpm.compile.BuildManager
import dev.cbyrne.kpm.dependency.DependencyManager
import dev.cbyrne.kpm.extension.deleteRecursively
import dev.cbyrne.kpm.extension.relativeToRootString
import dev.cbyrne.kpm.file.KPMFileManager
import dev.cbyrne.kpm.project.Project
import dev.cbyrne.kpm.scripting.manager.ScriptManager
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.nio.file.Path
import kotlin.io.path.div
import kotlin.io.path.readText

class KPM(val project: Project, val fileManager: KPMFileManager) {
    internal val dependencyManager = DependencyManager(this)
    private val buildManager = BuildManager(this)

    fun initialize() {
        dependencyManager
            .resolve()
            .getOrElse {
                return logger.warn("Failed to resolve dependencies: ${it.localizedMessage}")
            }
            .mapNotNull { (dependency, result) ->
                when (result.status) {
                    is FetchStatus.RepositoryFetchStatus.SUCCESSFUL -> Pair(dependency, result.artifact)
                    else -> {
                        logger.error("Unable to resolve $dependency.")
                        null
                    }
                }
            }.forEach { (dependency, artifact) ->
                if (artifact == null) return@forEach logger.error("Unable to resolve $dependency.")
                dependencyManager.fetch(dependency, artifact)
                    .getOrElse { t -> return logger.error("Failed to retrieve ${artifact.coordinate}: ${t.localizedMessage}") }
                    .let {
                        logger.info("Resolved ${it.coordinate} -> ${it.main.localFile}")
                    }
            }
    }

    fun build() {
        val output = buildManager.compile()
            .getOrElse { return logger.error("Failed to compile. ${it.localizedMessage}.") }

        project.script.dependencies
            .filter { it.bundle }
            .mapNotNull { dependencyManager.dependencies[it] }
            .let {
                buildManager.bundle(it, output)
                    .onFailure {
                        output.deleteRecursively()
                        return logger.error("Failed to bundle dependencies inside package. ${it.localizedMessage}")
                    }
            }

        logger.info("Creating package...")
        buildManager.createManifest(output)
            .onFailure {
                output.deleteRecursively()
                return logger.error("Failed to create manifest. ${it.localizedMessage}")
            }

        buildManager.createPackage(output, fileManager.packageDir / "${project.script.artifactName}.jar")
            .getOrElse { return logger.error("Failed to create package. ${it.localizedMessage}") }
            .let { logger.info("Build successful! (./${it.relativeToRootString(fileManager)})") }
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