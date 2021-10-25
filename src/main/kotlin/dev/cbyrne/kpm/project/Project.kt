package dev.cbyrne.kpm.project

import dev.cbyrne.kpm.project.pkg.ProjectScript
import kotlinx.serialization.Serializable

@Serializable
data class Project(val script: ProjectScript)
