package dev.cbyrne.kpm.compile

import dev.cbyrne.kpm.KPM
import dev.cbyrne.kpm.compile.message.KPMMessageCollector
import dev.cbyrne.kpm.compile.message.KPMMessageRenderer
import dev.cbyrne.kpm.extension.createDirectoryIfNotExists
import org.jetbrains.kotlin.cli.common.ExitCode
import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.config.Services
import kotlin.io.path.absolutePathString
import kotlin.io.path.div
import kotlin.io.path.relativeTo

class BuildManager(private val kpm: KPM) {
    // TODO: Make this configurable (e.g: `project { sources = "src/main" }`)
    private val sources = kpm.root / "src"
    private val kpmDir = kpm.root / "kpm"

    private val compileDir = kpmDir / "compiled"
    private val packageDir = kpmDir / "package"
    private val outputJar = packageDir / "${kpm.project.script.name}.jar"

    private val collector = KPMMessageCollector(KPMMessageRenderer(kpm.root))

    init {
        kpmDir.createDirectoryIfNotExists()
        compileDir.createDirectoryIfNotExists()
        packageDir.createDirectoryIfNotExists()
    }

    fun compile() {
        when (K2JVMCompiler().exec(collector, Services.EMPTY, generateArguments())) {
            ExitCode.OK -> {
                println("[kpm] Build successful! (./${outputJar.relativeTo(kpm.root)})")
            }
            ExitCode.COMPILATION_ERROR -> {
                System.err.println("[kpm] Failed to compile, check logs for more information.")
            }
            else -> {
                System.err.println("[kpm] Failed to compile, an internal error has occurred in kotlinc.")
            }
        }

        cleanup()
    }

    private fun cleanup() {
        compileDir.toFile().deleteRecursively()
    }

    private fun generateArguments(): K2JVMCompilerArguments {
        val args = K2JVMCompilerArguments()
        with(args) {
            suppressWarnings = false
            languageVersion = "1.5"
            // TODO: Download Kotlin libs at runtime
            kotlinHome = "/Users/cbyrne/Desktop"
            apiVersion = "1.5"
            multiPlatform = false
            destination = this@BuildManager.outputJar.absolutePathString()
            noStdlib = false
            includeRuntime = false
            freeArgs = listOf(sources.absolutePathString())
        }

        return args
    }
}