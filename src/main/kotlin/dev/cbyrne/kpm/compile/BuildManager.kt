package dev.cbyrne.kpm.compile

import dev.cbyrne.kpm.KPM
import kotlin.io.path.div
import kotlin.io.path.extension

class BuildManager(kpm: KPM) {
    // TODO: Make this optional and configurable
    private val sources = kpm.root / "src" / "main" / "kotlin"

    fun build() {
    }

    fun collectBuildFiles() = sources.filter { it.extension == "kt" }
}