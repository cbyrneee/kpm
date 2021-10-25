package dev.cbyrne.kpm

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import kotlin.io.path.Path
import kotlin.io.path.div
import kotlin.io.path.name

class KPMCommand : CliktCommand() {
    private val scriptPath: String? by option(help = "Define a custom path to your kpm script")

    override fun run() {
        val directory = Path(System.getProperty("user.dir"))
        val script = scriptPath?.let { Path(it) } ?: (directory / "kpm.kts")

        println("[kpm] Loading project from ${script.name}...")
        val kpm = KPM.load(script, directory)
        println("[kpm] Initializing project ${kpm.currentProject.script.name}${if (kpm.isCached) " (loaded from cache)" else ""}...")

        kpm.generateCache()
        println("[kpm] Project ${kpm.currentProject.script.name} initialized!")
    }
}

fun main(args: Array<String>) {
    // FIXME: Only used for debugging purposes, see https://youtrack.jetbrains.com/issue/KT-40937
    System.setProperty("idea.io.use.nio2", "true")
    KPMCommand().main(args)
}