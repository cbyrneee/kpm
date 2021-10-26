package dev.cbyrne.kpm.project.pkg

import dev.cbyrne.kpm.dependency.artifact.Artifact
import org.apache.maven.model.Repository

data class ProjectScript(
    val name: String,
    val settings: Settings,
    val dependencies: List<Dependency>,
    val repositories: List<Repository>,
) {
    data class Dependency(val artifact: Artifact, val bundle: Boolean)
    data class Settings(val doCompilerOutput: Boolean = false, val artifactName: String? = null)

    val artifactName: String
        get() = settings.artifactName ?: name
}
