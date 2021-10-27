package dev.cbyrne.kpm.dependency

import com.squareup.tools.maven.resolution.*
import dev.cbyrne.kpm.KPM
import dev.cbyrne.kpm.project.pkg.ProjectScript
import org.apache.maven.model.Repository

class DependencyManager(private val kpm: KPM) {
    private val repositories = mutableListOf<Repository>(Repositories.MAVEN_CENTRAL)
    private val resolver = ArtifactResolver(repositories = repositories, suppressAddRepositoryWarnings = true)
    private val availableDependencies = mutableMapOf<ProjectScript.Dependency, ResolvedArtifact>()

    val dependencies: Map<ProjectScript.Dependency, ResolvedArtifact>
        get() = availableDependencies

    init {
        kpm.project.script.repositories.forEach { repositories.add(it) }
    }

    fun resolve(): Result<List<Pair<ProjectScript.Dependency, ResolutionResult>>> =
        kpm.project.script.dependencies
            .ifEmpty { return Result.failure(Exception("No dependencies to resolve")) }
            .map {
                Pair(it, resolver.resolve(resolver.artifactFor(it.artifact.toString())))
            }.let {
                Result.success(it)
            }

    fun fetch(dependency: ProjectScript.Dependency, artifact: ResolvedArtifact): Result<ResolvedArtifact> {
        return when (resolver.downloadArtifact(artifact)) {
            is FetchStatus.RepositoryFetchStatus.SUCCESSFUL -> {
                availableDependencies[dependency] = artifact
                Result.success(artifact)
            }
            else -> Result.failure(Exception("Failed to resolve ${artifact.coordinate}"))
        }
    }
}
