package dev.cbyrne.kpm.dsl

import dev.cbyrne.kpm.project.pkg.ProjectScript

class ProjectScriptBuilder {
    var name: String? = null

    fun build() = ProjectScript(name ?: error("You must set a project name in your project {} block."))
}

fun project(builder: ProjectScriptBuilder.() -> Unit) = ProjectScriptBuilder().apply(builder).build()
