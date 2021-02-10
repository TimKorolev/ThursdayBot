package commands.executers.words

import commands.ICommandExecutor
import commands.executers.BaseExecutor
import db.requests.WordsRequest


object DeleteWord : BaseExecutor(), ICommandExecutor {

    override fun execute(params: List<String>): String {
        val word = params[1]
        return WordsRequest.deleteWord(word, chatId)
    }
}