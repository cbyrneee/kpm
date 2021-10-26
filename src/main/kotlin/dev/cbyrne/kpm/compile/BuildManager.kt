package dev.cbyrne.kpm.compile

import dev.cbyrne.kpm.KPM
import dev.cbyrne.kpm.compile.message.KPMMessageCollector
import dev.cbyrne.kpm.compile.message.KPMMessageRenderer
import dev.cbyrne.kpm.extension.relativeToRootString
import org.jetbrains.kotlin.cli.common.ExitCode
import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.config.Services
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.div

class BuildManager(private val kpm: KPM) {
    private val collector = KPMMessageCollector(kpm.project, KPMMessageRenderer(kpm.fileManager))
    private val compiler = K2JVMCompiler()

    fun compile() {
        val outputJar = kpm.fileManager.packageDir / "${kpm.project.script.artifactName}.jar"
        val arguments = generateArguments(outputJar)

        when (compiler.exec(collector, Services.EMPTY, arguments)) {
            ExitCode.OK -> {
                println("[kpm] Build successful! (./${outputJar.relativeToRootString(kpm.fileManager)})")
            }
            ExitCode.COMPILATION_ERROR -> {
                System.err.println("[kpm] Failed to compile, check logs for more information.")
            }
            else -> {
                System.err.println("[kpm] Failed to compile, an internal error has occurred in kotlinc.")
            }
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
            freeArgs = listOf(kpm.fileManager.sourcesDir.absolutePathString())
        }

        return args
    }
}