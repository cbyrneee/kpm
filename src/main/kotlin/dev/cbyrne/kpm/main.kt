package dev.cbyrne.kpm

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import kotlin.io.path.Path

class KPMCommand : CliktCommand() {
    private val scriptPath: String? by option(help = "Define a custom path to your kpm script")

    override fun run() {
        val path = Path(scriptPath ?: "kpm.kts")
        println("[kpm] Loading project from $path...")

        val kpm = KPM.load(path)
        println("[kpm] Initializing project ${kpm.currentProject.script.name}...")
        println("[kpm] Project ${kpm.currentProject.script.name} initialized.")
    }
}

fun main(args: Array<String>) {
    // FIXME: Only used for debugging purposes, see https://youtrack.jetbrains.com/issue/KT-40937
    System.setProperty("idea.io.use.nio2", "true")
    KPMCommand().main(args)
}