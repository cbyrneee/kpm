package dev.cbyrne.kpm.compile

import dev.cbyrne.kpm.KPM
import dev.cbyrne.kpm.compile.message.KPMMessageCollector
import dev.cbyrne.kpm.compile.message.KPMMessageRenderer
import org.apache.logging.log4j.LogManager
import org.jetbrains.kotlin.cli.common.ExitCode
import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.config.Services
import org.jetbrains.kotlin.incremental.classpathAsList
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.div

class BuildManager(private val kpm: KPM) {
    private val logger = LogManager.getLogger("kotlinc")
    private val collector = KPMMessageCollector(kpm.project, KPMMessageRenderer(kpm.fileManager, logger), logger)
    private val compiler = K2JVMCompiler()

    fun compile(): Result<Path> {
        val outputJar = kpm.fileManager.packageDir / "${kpm.project.script.artifactName}.jar"
        val arguments = generateArguments(outputJar)

        return when (compiler.exec(collector, Services.EMPTY, arguments)) {
            ExitCode.OK -> Result.success(outputJar)
            ExitCode.COMPILATION_ERROR -> Result.failure(Exception("Check logs for more information"))
            else -> Result.failure(Exception("Internal kotlinc error"))
        }
    }

    private fun generateArguments(output: Path): K2JVMCompilerArguments {
        val args = K2JVMCompilerArguments()
        with(args) {
            suppressWarnings = false
            languageVersion = "1.5"
            // TODO: Download Kotlin libs at runtime
            kotlinHome = "/opt/homebrew/Cellar/kotlin/1.5.31/libexec/"
            apiVersion = "1.5"
            multiPlatform = false
            includeRuntime = false
            destination = output.absolutePathString()
            classpathAsList = kpm.dependencyManager.dependencies.map { it.main.localFile.toFile() }
            freeArgs = listOf(kpm.fileManager.sourcesDir.absolutePathString())
        }

        return args
    }
}