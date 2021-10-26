@file:Suppress("unused")

package dev.cbyrne.kpm.dsl

import dev.cbyrne.kpm.dependency.artifact.Artifact
import dev.cbyrne.kpm.dependency.repository.Repository
import dev.cbyrne.kpm.project.pkg.ProjectScript
import java.net.URL

class ProjectScriptBuilder {
    var name: String? = null
    var settings = ProjectScript.Settings()
    var dependenciesBuilder = DependenciesBuilder()
    var repositoriesBuilder = RepositoriesBuilder()

    fun dependencies(builder: DependenciesBuilder.() -> Unit) {
        dependenciesBuilder = DependenciesBuilder().apply(builder)
    }

    fun repositories(builder: RepositoriesBuilder.() -> Unit) {
        repositoriesBuilder = RepositoriesBuilder().apply(builder)
    }

    fun settings(builder: SettingsBuilder.() -> Unit) {
        settings = SettingsBuilder().apply(builder).build()
    }

    fun build() = ProjectScript(
        name ?: error("You must set a project name in your project {} block."),
        settings,
        dependenciesBuilder.build(),
        repositoriesBuilder.build()
    )

    class SettingsBuilder {
        var doCompilerOutput = false
        var artifactNameOverride: String? = null

        fun build() = ProjectScript.Settings(doCompilerOutput, artifactNameOverride)
    }

    class RepositoriesBuilder {
        private val repositories =
            mutableListOf<Repository>(Repository.Maven.mavenCentral())

        fun maven(url: URL) =
            repositories.add(Repository.Maven(url))

        fun kpm(url: URL) =
            repositories.add(Repository.KPM(url))

        fun maven(url: String) = maven(URL(url))

        fun kpm(url: String) = kpm(URL(url))

        fun build() = repositories
    }

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
