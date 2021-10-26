package dev.cbyrne.kpm.dependency

import dev.cbyrne.kpm.KPM
import dev.cbyrne.kpm.dependency.artifact.resolver.ArtifactManager
import org.jetbrains.kotlin.utils.ifEmpty

class DependencyManager(private val kpm: KPM) {
    private val artifactManager = ArtifactManager(kpm.project)

    fun resolveDependencies() {
        kpm.project.script.repositories.ifEmpty {
            return KPM.logger.warn("Unable to resolve any dependencies. Repositories are somehow empty! (this should not happen)")
        }

        kpm.project.script.dependencies
            .ifEmpty {
                return KPM.logger.info("No dependencies to resolve.")
            }
            .forEach { dependency ->
                val located = artifactManager.locate(dependency.artifact)
                    .getOrElse { return@forEach KPM.logger.error("Failed to locate dependency: $dependency. Skipping!") }

                KPM.logger.info("Successfully resolved dependency: $located")
            }
    }
}
