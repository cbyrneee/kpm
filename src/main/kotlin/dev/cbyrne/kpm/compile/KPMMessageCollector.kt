package dev.cbyrne.kpm.compile

import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSourceLocation
import org.jetbrains.kotlin.cli.common.messages.MessageCollector

class KPMMessageCollector : MessageCollector {
    private val errors = mutableSetOf<String>()

    override fun clear() {
        errors.clear()
    }

    override fun hasErrors() = errors.isNotEmpty()

    override fun report(
        severity: CompilerMessageSeverity,
        message: String,
        location: CompilerMessageSourceLocation?
    ) {
        if (severity.isError) {
            errors.add(message)
            return System.err.println("[kotlinc] (ERROR) $message")
        } else if (severity.isWarning) {
            return println("[kotlinc] (WARNING) $message")
        }

        return println("[kotlinc] (INFO) $message")
    }
}