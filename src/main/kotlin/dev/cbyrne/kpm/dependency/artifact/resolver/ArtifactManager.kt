package dev.cbyrne.kpm.dependency.artifact.resolver

import dev.cbyrne.kpm.dependency.artifact.Artifact
import dev.cbyrne.kpm.project.Project

class ArtifactManager(private val project: Project) {
    fun locate(artifact: Artifact): Result<ResolvedArtifact> {
        // TODO: Find the source of the artifact and return a ResolvedArtifact instance
        return Result.failure(Exception("Artifact not found"))
    }
}