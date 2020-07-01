package commands.executers

import api.ApiHandler
import commands.ICommandExecuter

object VocabularyExecuter : ICommandExecuter {

    override fun execute(params: List<String>): String {
        val apiHandler = ApiHandler()
        apiHandler.execute()
        var word = params[0]
        return "Запомню $word"
    }
}