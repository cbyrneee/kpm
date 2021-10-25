package dev.cbyrne.kpm.project

import dev.cbyrne.kpm.project.pkg.ProjectScript
import java.nio.file.Path

data class Project(val script: ProjectScript, val file: Path)