package dev.cbyrne.kpm.dependency.artifact

open class Artifact(
    open val group: String,
    open val name: String,
    open val version: String
) {
    override fun toString(): String {
        return "$group:$name:$version"
    }
}
