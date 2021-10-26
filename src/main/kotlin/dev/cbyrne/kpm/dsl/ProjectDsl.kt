@file:Suppress("unused")

package dev.cbyrne.kpm.dsl

import dev.cbyrne.kpm.dependency.Artifact
import dev.cbyrne.kpm.project.pkg.ProjectScript

class ProjectScriptBuilder {
    var name: String? = null
    var dependenciesBuilder: DependenciesBuilder? = null

    fun dependencies(builder: DependenciesBuilder.() -> Unit) {
        dependenciesBuilder = DependenciesBuilder().apply(builder)
    }

    fun build() = ProjectScript(
        name ?: error("You must set a project name in your project {} block."),
        dependenciesBuilder?.build() ?: emptyList()
    )

    class DependenciesBuilder {
        private val dependencies = mutableListOf<ProjectScript.Dependency>()

        fun bundle(builder: DependencyBuilder.() -> Unit) =
            dependencies.add(DependencyBuilder().apply(builder).build())

        fun bundle(artifact: Artifact) =
            dependencies.add(ProjectScript.Dependency(artifact, true))

        fun bundle(group: String, name: String, version: String) =
            dependencies.add(ProjectScript.Dependency(Artifact(group, name, version), true))

        fun bundle(simpleString: String) {
            val sections = simpleString.split(":")
            if (sections.size != 3) error("Invalid dependency string: $simpleString")

            val artifact = Artifact(sections[0], sections[1], sections[2])
            dependencies.add(ProjectScript.Dependency(artifact, true))
        }

        fun build() = dependencies

        class DependencyBuilder {
            var group: String? = null
            var name: String? = null
            var version: String? = null

            fun build() =
                ProjectScript.Dependency(
                    Artifact(
                        group ?: error("You must set a group in your project {} block."),
                        name ?: error("You must set a name in your project {} block."),
                        version ?: error("You must set a version in your project {} block.")
                    ),
                    true
                )
        }
    }
}

fun project(builder: ProjectScriptBuilder.() -> Unit) = ProjectScriptBuilder().apply(builder).build()
