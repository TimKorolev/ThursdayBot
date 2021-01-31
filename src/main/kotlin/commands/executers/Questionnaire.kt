package commands.executers

import commands.ICommandExecutor


object Questionnaire : BaseExecutor(), ICommandExecutor {

    override fun execute(params: List<String>): String {
        return ""
    }

}