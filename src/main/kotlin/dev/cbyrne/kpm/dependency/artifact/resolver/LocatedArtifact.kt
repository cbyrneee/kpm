package dev.cbyrne.kpm.dependency.artifact.resolver

import dev.cbyrne.kpm.dependency.artifact.Artifact
import dev.cbyrne.kpm.dependency.repository.Repository

data class LocatedArtifact(
    override val group: String,
    override val name: String,
    override val version: String,
    val repository: Repository
) : Artifact(group, name, version)
