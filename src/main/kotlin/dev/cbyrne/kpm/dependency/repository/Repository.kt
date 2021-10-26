package dev.cbyrne.kpm.dependency.repository

import java.net.URL

// TODO: Implement this
sealed class Repository(val url: URL) {
    class Maven(url: URL) : Repository(url) {
        companion object {
            fun mavenCentral() = Maven(URL("https://repo1.maven.org/maven2/"))
        }
    }

    class KPM(url: URL) : Repository(url) {
    }
}
