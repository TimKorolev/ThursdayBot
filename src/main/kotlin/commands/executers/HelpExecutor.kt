package commands.executers

import commands.ICommandExecutor


object HelpExecutor : BaseExecutor(), ICommandExecutor {

    override fun execute(params: List<String>): String {

        return """Команды:
.addWord <word> (<custom translate>) - добавляет английское слово с переводом
"""
    }

}