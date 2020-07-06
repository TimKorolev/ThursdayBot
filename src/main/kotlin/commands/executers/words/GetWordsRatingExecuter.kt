package commands.executers.words

import commands.ICommandExecuter
import db.DbRequest


object GetWordsRatingExecuter : ICommandExecuter {

    override lateinit var chatId: String

    fun setChatId(_chatId: String): GetWordsRatingExecuter {
        chatId = _chatId
        return this
    }

    override fun execute(params: List<String>): String {
        var limit = "10"
        if (params.isNotEmpty()){
            limit = params[0]
        }
        return DbRequest.getWordsRating(chatId, limit)
    }

}