package dev.cbyrne.kpm.project.pkg

import dev.cbyrne.kpm.dependency.Artifact

data class ProjectScript(val name: String, val dependencies: List<Dependency>, val settings: Settings) {
    data class Dependency(val artifact: Artifact, val bundle: Boolean)

    data class Settings(val doCompilerOutput: Boolean = false)
}
