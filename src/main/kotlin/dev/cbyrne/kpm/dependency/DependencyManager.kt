package dev.cbyrne.kpm.dependency

import dev.cbyrne.kpm.KPM
import dev.cbyrne.kpm.dependency.artifact.resolver.ArtifactManager

class DependencyManager(private val kpm: KPM) {
    private val artifactManager = ArtifactManager(kpm.project)

    fun resolveDependencies() {
        kpm.project.script.dependencies.forEach { dependency ->
            val located = artifactManager.locate(dependency.artifact)
                .getOrElse { return@forEach System.err.println("[kpm] Failed to locate dependency: $dependency. Skipping!") }

            println("[kpm] Successfully resolved dependency: $located")
        }
    }
}
