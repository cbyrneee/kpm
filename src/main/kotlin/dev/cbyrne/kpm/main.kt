@file:Suppress("unused")

package dev.cbyrne.kpm

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.findOrSetObject
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.option
import kotlin.io.path.Path
import kotlin.io.path.div
import kotlin.io.path.name

private fun load(customPath: String?): KPM {
    val directory = Path(System.getProperty("user.dir"))
    val script = customPath?.let { Path(it) } ?: (directory / "kpm.kts")

    println("[kpm] Evaluating project from ${script.name}...")
    val kpm = KPM.load(script, directory)
    println("[kpm] Evaluated project ${kpm.project.script.name}!")

    return kpm
}

private object KPMCommand : CliktCommand() {
    val scriptPath: String? by option(help = "Define a custom path to your kpm script")
    val kpm by findOrSetObject { load(scriptPath) }

    override fun run() {
        kpm.initialize()
    }

    object Build : CliktCommand(name = "build", help = "Build your project into a .JAR file") {
        val scriptPath: String? by option(help = "Define a custom path to your kpm script")
        val kpm by requireObject<KPM>()

        override fun run() {
            println("[kpm] Building...")
            kpm.build()
        }
    }
}

fun main(args: Array<String>) {
    // FIXME: Only used for debugging purposes, see https://youtrack.jetbrains.com/issue/KT-40937
    System.setProperty("idea.io.use.nio2", "true")
    KPMCommand.subcommands(KPMCommand.Build).main(args)
}