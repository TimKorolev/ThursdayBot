package commands.executers.alcohol

import commands.ICommandExecuter
import db.DbRequest


object AlcoholExecuter : ICommandExecuter {

    override lateinit var chatId: String

    fun setChatId(_chatId: String): AlcoholExecuter {
        chatId = _chatId
        return this
    }

    override fun execute(params: List<String>): String {
        val name = params[0]
        val rating = params[1].toInt()
        val newParams = params.drop(2)
        val description = newParams.toString().replace(",", "").replace("[", "").replace("]", "")
        return DbRequest.addAlcohol(name, rating, description, chatId)
    }
}