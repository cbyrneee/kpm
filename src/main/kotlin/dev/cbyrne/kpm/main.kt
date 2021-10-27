@file:Suppress("unused")

package dev.cbyrne.kpm

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import dev.cbyrne.kpm.extension.createFileIfNotExists
import java.nio.file.Path
import kotlin.io.path.*

private fun load(customPath: String?): KPM {
    KPM.logger.info("Evaluating project script...")

    val directory = Path(System.getProperty("user.dir"))
    val script = customPath?.let { Path(it) } ?: (directory / "kpm.kts")
    return KPM.load(script, directory).let {
        KPM.logger.info("Evaluated project ${it.project.script.name}!")
        it
    }
}

private object KPMCommand : CliktCommand() {
    val scriptPath: String? by option(help = "Define a custom path to your kpm script")

    override fun run() {
    }

    object Build : CliktCommand(name = "build", help = "Build your project into a .JAR file") {
        val scriptPath: String? by option(help = "Define a custom path to your kpm script")

        override fun run() {
            val kpm = load(KPMCommand.scriptPath)
            kpm.initialize()
            kpm.build()
        }
    }

    object Init : CliktCommand(name = "init", help = "Create a KPM project") {
        val name by option().prompt("What is your project's name?")

        override fun run() {
            KPM.logger.info("Creating project: $name")

            val directory = Path(name)
            if (directory.exists())
                return KPM.logger.error("Unable to create a KPM project inside a directory which already exists. ($directory)")

            createFiles(directory)
            KPM.logger.info("Project created! Run kpm build to build your project.\n      $ cd ./${directory}\n      $ kpm build")
        }

        private fun createFiles(directory: Path) {
            directory.createDirectories()
            createBuildScript(directory)
            createMainKt(directory)
        }

        private fun createBuildScript(directory: Path) =
            writeFile(
                directory / "kpm.kts",
                """
                project {
                    name = "$name"
                    main = "MainKt"     // The name of your main class, if you do not have one, you can remove this line
                    
                    dependencies {
                        bundle("org.jetbrains.kotlin:kotlin-stdlib:1.5.31")     // If you do not want kotlin in your final jar, use runtime() instead
                        // bundle("x:y:z")     - A dependency which will be added to the final JAR
                        // runtime("x:y:z")    - A dependency which is available at runtime but not at compile time (not in the final JAR)
                    }

                    // repositories {
                    //     mavenCentral is added by default
                    //     maven("https://myrepo.com")
                    // }
                }
                """.trimIndent().trimMargin()
            )

        private fun createMainKt(directory: Path) =
            writeFile(
                directory / "src/Main.kt",
                """
                fun main() {
                    println("Hello, world!")
                }
                """
            )

        private fun writeFile(file: Path, contents: String) {
            file.parent.createDirectories()
            file.createFileIfNotExists()
            file.writeText(contents.trimIndent().trimMargin())
        }
    }
}

fun main(args: Array<String>) {
    // FIXME: Only used for debugging purposes, see https://youtrack.jetbrains.com/issue/KT-40937
    System.setProperty("idea.io.use.nio2", "true")
    KPMCommand.subcommands(KPMCommand.Build, KPMCommand.Init).main(args)
}