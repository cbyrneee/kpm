package dev.cbyrne.kpm.compile.message

import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSourceLocation
import org.jetbrains.kotlin.cli.common.messages.PlainTextMessageRenderer
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.pathString
import kotlin.io.path.relativeTo

class KPMMessageRenderer(private val root: Path) : PlainTextMessageRenderer() {
    private val prefix = "[kotlinc]"

    override fun renderPreamble() = ""
    override fun renderConclusion() = ""
    override fun getName() = "KPM"
    override fun getPath(location: CompilerMessageSourceLocation) = Path(location.path).relativeTo(root).pathString

    override fun render(
        severity: CompilerMessageSeverity,
        message: String,
        location: CompilerMessageSourceLocation?
    ) =
        if (location != null)
            "$prefix (${location.path}:${location.line}:${location.column}): $message\n" +
                    "${" ".repeat(prefix.length + 1)}${location.lineContent}\n" +
                    "${" ".repeat(prefix.length + 1)}${" ".repeat(location.column - 1)}^"
        else "$prefix $message"

    override fun renderUsage(usage: String) = usage
}