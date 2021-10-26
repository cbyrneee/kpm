package dev.cbyrne.kpm.compile.message

import dev.cbyrne.kpm.extension.relativeToRootString
import dev.cbyrne.kpm.file.KPMFileManager
import org.apache.logging.log4j.Logger
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSourceLocation
import org.jetbrains.kotlin.cli.common.messages.PlainTextMessageRenderer
import kotlin.io.path.Path

class KPMMessageRenderer(private val fileManager: KPMFileManager, logger: Logger) : PlainTextMessageRenderer() {
    private val prefix = "[${logger.name}] "
    private val codePadding = padding(prefix.length + 1)

    override fun getPath(location: CompilerMessageSourceLocation) =
        Path(location.path).relativeToRootString(fileManager)

    override fun render(
        severity: CompilerMessageSeverity,
        message: String,
        location: CompilerMessageSourceLocation?
    ) =
        if (location == null)
            message
        else """
            (${location.reference()}): $message
            ${location.lineContent?.padded()}
            ${"^".padded(location.column - 1)}
            """.trimIndent().trimMargin()

    private fun padding(length: Int) = " ".repeat(length)
    private fun String.padded(extra: Int = 0) = "$codePadding${padding(extra)}$this"
    private fun CompilerMessageSourceLocation.reference() = "${path}:${line}:${column}"

    override fun renderUsage(usage: String) = usage
    override fun renderPreamble() = ""
    override fun renderConclusion() = ""
    override fun getName() = "KPM"
}
