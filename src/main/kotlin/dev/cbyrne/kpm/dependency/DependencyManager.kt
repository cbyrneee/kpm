package dev.cbyrne.kpm.dependency

import dev.cbyrne.kpm.KPM
import dev.cbyrne.kpm.dependency.artifact.resolver.ArtifactManager

class DependencyManager(private val kpm: KPM) {
    private val artifactManager = ArtifactManager(kpm.project)

    fun resolveDependencies() {
        kpm.project.script.dependencies.forEach { dependency ->
            val located = artifactManager.locate(dependency.artifact)
                .getOrElse { return@forEach KPM.logger.error("Failed to locate dependency: $dependency. Skipping!") }

            KPM.logger.info("Successfully resolved dependency: $located")
        }
    }
}
