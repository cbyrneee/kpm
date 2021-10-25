@file:Suppress("UNCHECKED_CAST")

package dev.cbyrne.kpm.scripting.manager

import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

class ScriptManager {
    private val imports = listOf("dev.cbyrne.kpm.dsl.*")
    private val engine: ScriptEngine = ScriptEngineManager().getEngineByExtension("kts")

    private fun prependImports(originalScript: String) = "${imports.joinToString("\n") { "import $it" }}$originalScript"

    fun <T> parse(script: String): T = engine.eval(prependImports(script)) as T
}