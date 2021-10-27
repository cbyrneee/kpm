package dev.cbyrne.kpm.compile

import com.squareup.tools.maven.resolution.ResolvedArtifact
import dev.cbyrne.kpm.KPM
import dev.cbyrne.kpm.compile.message.KPMMessageCollector
import dev.cbyrne.kpm.compile.message.KPMMessageRenderer
import dev.cbyrne.kpm.extension.*
import org.apache.logging.log4j.LogManager
import org.jetbrains.kotlin.cli.common.ExitCode
import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.config.Services
import org.jetbrains.kotlin.incremental.classpathAsList
import org.jetbrains.kotlin.konan.file.File
import java.nio.file.Path
import java.util.jar.Manifest
import kotlin.io.path.*

class BuildManager(private val kpm: KPM) {
    private val logger = LogManager.getLogger("kotlinc")
    private val collector = KPMMessageCollector(kpm.project, KPMMessageRenderer(kpm.fileManager, logger), logger)
    private val compiler = K2JVMCompiler()

    fun compile(): Result<Path> {
        val output = kpm.fileManager.packageDir / "artifact-temp"
        val arguments = generateArguments(output)

        return when (compiler.exec(collector, Services.EMPTY, arguments)) {
            ExitCode.OK -> Result.success(output)
            ExitCode.COMPILATION_ERROR -> Result.failure(Exception("Check logs for more information"))
            else -> Result.failure(Exception("Internal kotlinc error"))
        }
    }

    fun createPackage(contents: Path, output: Path): Result<Path> {
        if (!contents.exists() || !contents.isDirectory())
            return Result.failure(Exception("Contents path either does not exist or is not a directory"))

        return output
            .createFileIfNotExists()
            .zipOutputStream()
            .use { stream ->
                contents
                    .walkTopDown()
                    .forEach {
                        kotlin.runCatching {
                            val relative = it.toPath().relativeTo(contents).toString()
                            if (it.isFile) {
                                stream.addEntry(relative, it.readBytes())
                            } else {
                                stream.addEntry("$relative${File.separator}")
                            }
                        }.onFailure {
                            contents.deleteRecursively()
                            return@use Result.failure(it)
                        }
                    }

                contents.deleteRecursively()
                Result.success(output)
            }
    }

    private fun generateArguments(output: Path): K2JVMCompilerArguments {
        val args = K2JVMCompilerArguments()
        with(args) {
            suppressWarnings = false
            languageVersion = "1.5"
            apiVersion = "1.5"
            multiPlatform = false
            noStdlib = true
            includeRuntime = false
            destination = output.absolutePathString()
            classpathAsList = kpm.dependencyManager.dependencies.map { it.value.main.localFile.toFile() }
            freeArgs = listOf(kpm.fileManager.sourcesDir.absolutePathString())
        }

        return args
    }

    fun bundle(artifacts: List<ResolvedArtifact>, destination: Path): Result<List<ResolvedArtifact>> {
        artifacts.forEach { artifact ->
            artifact.main.localFile.zipFile().use { zip ->
                zip.entriesSequence()
                    .filter { !it.isDirectory }
                    .forEach {
                        zip.inputStream(it) { stream ->
                            kotlin.runCatching {
                                destination.resolve(it.name)
                                    .createFileAndParentIfNotExists()
                                    .writeBytes(stream.readBytes())
                            }.onFailure { t ->
                                return Result.failure(t)
                            }
                        }
                    }
            }
        }

        return Result.success(artifacts)
    }

    fun createManifest(output: Path, mainClass: String? = null): Result<Manifest> =
        output
            .resolve("META-INF/MANIFEST.MF")
            .createFileAndParentIfNotExists()
            .let { file ->
                kotlin.runCatching {
                    generateManifest(mainClass)
                        .let {
                            it.write(file.outputStream())
                            it
                        }
                }.onFailure {
                    it.printStackTrace()
                    return Result.failure(it)
                }
            }

    private fun generateManifest(mainClass: String? = null): Manifest {
        val manifest = Manifest()
        with(manifest) {
            mainAttributes.putValue("Manifest-Version", "1.0")
            mainAttributes.putValue("Created-By", "KPM (Kotlin Project Manager)")
            mainClass?.let { clazz -> mainAttributes.putValue("Main-Class", clazz) }
        }

        return manifest
    }
}