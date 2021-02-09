package commands.executers

import commands.ICommandExecutor


object HelpExecutor : BaseExecutor(), ICommandExecutor {

    override fun execute(params: List<String>): String {

        return """Commands:
<word> as (<custom translate>)
"""
    }

}