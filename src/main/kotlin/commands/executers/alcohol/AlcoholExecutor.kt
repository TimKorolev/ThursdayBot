package commands.executers.alcohol

import commands.ICommandExecutor
import commands.executers.BaseExecutor
import db.DbRequest


object AlcoholExecutor : BaseExecutor(), ICommandExecutor {

    override fun execute(params: List<String>): String {
        val name = params[0]
        val rating = params[1].toInt()
        val newParams = params.drop(2)
        val description = newParams.toString().replace(",", "").replace("[", "").replace("]", "")
        return DbRequest.addAlcohol(name, rating, description, chatId)
    }
}