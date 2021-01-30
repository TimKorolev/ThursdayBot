package commands.executers.words

import commands.ICommandExecutor
import commands.executers.BaseExecutor
import db.DbRequest


object GetWordsRatingExecutor : BaseExecutor(), ICommandExecutor {

    override fun execute(params: List<String>): String {
        var limit = "10"
        if (params.isNotEmpty()){
            limit = params[0]
        }
        return DbRequest.getWordsRating(chatId, limit)
    }

}