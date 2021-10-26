package dev.cbyrne.kpm.compile.message

import dev.cbyrne.kpm.project.Project
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSourceLocation
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer

class KPMMessageCollector(private val project: Project, private val renderer: MessageRenderer) : MessageCollector {
    private var hasErrors = false
    override fun hasErrors() = hasErrors

    override fun clear() {
        // Do nothing
        hasErrors = false
    }

    override fun report(severity: CompilerMessageSeverity, message: String, location: CompilerMessageSourceLocation?) {
        val rendered = renderer.render(severity, message, location)
        if (severity.isError || severity.isWarning)
            System.err.println(rendered)
        else if (project.script.settings.doCompilerOutput)
            println(rendered)

        hasErrors = severity.isError
    }
}