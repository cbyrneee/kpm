package dev.cbyrne.kpm.dependency.repository

import java.net.URL

// TODO: Implement this
sealed class Repository(val url: URL) {
    class Maven(url: URL) : Repository(url) {
    }

    class KPM(url: URL) : Repository(url) {
    }
}
