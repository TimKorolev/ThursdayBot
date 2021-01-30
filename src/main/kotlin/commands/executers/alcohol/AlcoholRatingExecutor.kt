package commands.executers.alcohol

import commands.ICommandExecutor
import commands.executers.BaseExecutor
import db.DbRequest


object AlcoholRatingExecutor : BaseExecutor(), ICommandExecutor {

    override fun execute(params: List<String>) : String  {
        return DbRequest.getAlcoholRating(chatId)
    }
}