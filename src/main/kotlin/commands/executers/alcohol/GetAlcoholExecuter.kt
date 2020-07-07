package commands.executers.alcohol

import commands.ICommandExecuter
import db.DbRequest

object GetAlcoholExecuter : ICommandExecuter {

    override lateinit var chatId: String

    fun setChatId(_chatId: String): GetAlcoholExecuter {
        chatId = _chatId
        return this
    }

    override fun execute(params: List<String>): String {
        return DbRequest.getAlcohol(params[0], chatId)
    }
}