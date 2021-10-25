package dev.cbyrne.kpm

import dev.cbyrne.kpm.project.Project
import dev.cbyrne.kpm.scripting.manager.ScriptManager
import java.nio.file.Path
import kotlin.io.path.readText

class KPM(val currentProject: Project) {
    companion object {
        private val scripts = ScriptManager()

        fun load(path: Path) = KPM(Project(scripts.parse(path.readText()), path))
    }
}