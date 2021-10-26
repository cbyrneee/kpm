package dev.cbyrne.kpm.file

import dev.cbyrne.kpm.extension.createDirectoryIfNotExists
import java.nio.file.Path
import kotlin.io.path.div
import kotlin.io.path.exists

class KPMFileManager(val rootDir: Path) {
    val sourcesDir = rootDir / "src"
    val packageDir = rootDir / "package"

    init {
        if (!rootDir.exists())
            error("Root directory $rootDir does not exist.")

        packageDir.createDirectoryIfNotExists()
    }
}