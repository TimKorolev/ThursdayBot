package commands.executers.alcohol

import commands.ICommandExecuter
import db.DbRequest


object AlcoholRatingExecuter : ICommandExecuter {

    override lateinit var chatId: String

    fun setChatId(_chatId: String): AlcoholRatingExecuter {
        chatId = _chatId
        return this
    }

    override fun execute(params: List<String>) : String  {
        return DbRequest.getAlcoholRating(chatId)
    }
}