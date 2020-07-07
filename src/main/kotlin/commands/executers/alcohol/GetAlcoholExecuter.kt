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
        val result = DbRequest.getAlcohol(params[0], chatId)
        return if (result == "") {
            "Нет в коллекции"
        } else {
            result
        }
    }
}