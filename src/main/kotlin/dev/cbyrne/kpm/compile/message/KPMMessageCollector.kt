package dev.cbyrne.kpm.compile.message

import dev.cbyrne.kpm.project.Project
import org.apache.logging.log4j.Logger
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSourceLocation
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer

class KPMMessageCollector(
    private val project: Project,
    private val renderer: MessageRenderer,
    private val logger: Logger
) : MessageCollector {
    private var hasErrors = false
    override fun hasErrors() = hasErrors

    override fun clear() {
        // Do nothing
        hasErrors = false
    }

    override fun report(severity: CompilerMessageSeverity, message: String, location: CompilerMessageSourceLocation?) {
        val rendered = renderer.render(severity, message, location)
        if (severity.isError)
            logger.error(rendered)
        else if (severity.isWarning)
            logger.warn(rendered)
        else if (project.script.settings.doCompilerOutput)
            logger.info(rendered)

        hasErrors = severity.isError
    }
}