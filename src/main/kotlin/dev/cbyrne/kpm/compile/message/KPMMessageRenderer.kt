package dev.cbyrne.kpm.compile.message

import dev.cbyrne.kpm.extension.relativeToRootString
import dev.cbyrne.kpm.file.KPMFileManager
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSourceLocation
import org.jetbrains.kotlin.cli.common.messages.PlainTextMessageRenderer
import kotlin.io.path.Path

class KPMMessageRenderer(private val fileManager: KPMFileManager) : PlainTextMessageRenderer() {
    private val prefix = "[kpm.build] "

    override fun renderPreamble() = ""
    override fun renderConclusion() = ""
    override fun getName() = "KPM"
    override fun getPath(location: CompilerMessageSourceLocation) =
        Path(location.path).relativeToRootString(fileManager)

    override fun render(
        severity: CompilerMessageSeverity,
        message: String,
        location: CompilerMessageSourceLocation?
    ) =
        if (location == null) message
        else "(${location.path}:${location.line}:${location.column}): $message\n" +
                "${" ".repeat(prefix.length + 1)}${location.lineContent}\n" +
                "${" ".repeat(prefix.length + 1)}${" ".repeat(location.column - 1)}^"

    override fun renderUsage(usage: String) = usage
}