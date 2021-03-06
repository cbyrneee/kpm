package dev.cbyrne.kpm.project

import dev.cbyrne.kpm.KPM
import dev.cbyrne.kpm.project.pkg.ProjectScript

data class Project(val script: ProjectScript) {
    companion object {
        fun parse(script: String) = Project(KPM.scriptManager.parse(script))
    }
}
