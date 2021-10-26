package dev.cbyrne.kpm.compile

import dev.cbyrne.kpm.KPM
import org.jetbrains.kotlin.cli.common.ExitCode
import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.config.Services
import kotlin.io.path.absolutePathString
import kotlin.io.path.div

class BuildManager(kpm: KPM) {
    // TODO: Make this configurable (e.g: `project { sources = "src/main" }`)
    private val sources = kpm.root / "src"
    private val messageCollector = KPMMessageCollector()

    fun compile() {
        val arguments = generateArguments()
        val compiler = K2JVMCompiler()

        when (compiler.exec(messageCollector, Services.EMPTY, arguments)) {
            ExitCode.OK -> {
                println("[kpm] Compile successful!")
            }
            ExitCode.COMPILATION_ERROR -> {
                System.err.println("[kpm] Failed to compile, check logs for more information.")
            }
            else -> {
                System.err.println("[kpm] Failed to compile, an internal error has occurred in kotlinc.")
            }
        }
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
            destination = "build"
            noStdlib = false
            freeArgs = listOf(sources.absolutePathString())
        }

        return args
    }
}