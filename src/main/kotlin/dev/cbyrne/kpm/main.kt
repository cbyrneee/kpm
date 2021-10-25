package dev.cbyrne.kpm

import kotlin.io.path.Path

fun main() {
    // FIXME: Only used for debugging purposes, see https://youtrack.jetbrains.com/issue/KT-40937
    System.setProperty("idea.io.use.nio2", "true")

    val kpm = KPM.load(Path("kpm.kts"))
    println("[kpm] Loading project ${kpm.currentProject.script.name}...")
}