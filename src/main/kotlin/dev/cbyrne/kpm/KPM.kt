package dev.cbyrne.kpm

import dev.cbyrne.kpm.project.Project
import dev.cbyrne.kpm.scripting.manager.ScriptManager
import java.nio.file.Path
import kotlin.io.path.readText

class KPM(val project: Project, val root: Path) {
    companion object {
        val scriptManager = ScriptManager()

        fun load(script: Path, directory: Path) = KPM(Project.parse(script.readText()), directory)
    }
}