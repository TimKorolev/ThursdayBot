package commands.executers

import commands.ICommandExecuter
import db.DbRequest


object GetNLastWordsExecuter : ICommandExecuter {

    override lateinit var chatId: String

    fun setChatId(_chatId: String): GetNLastWordsExecuter {
        chatId = _chatId
        return this
    }

    override fun execute(params: List<String>): String {
        var limit = "10"
        if (params.isNotEmpty()){
            limit = params[0]
        }
        return DbRequest.getNLastWords(chatId, limit)
    }

}