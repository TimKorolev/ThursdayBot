package commands.executers.alcohol

import commands.ICommandExecutor
import commands.executers.BaseExecutor
import db.DbRequest

object GetAlcoholExecutor : BaseExecutor(), ICommandExecutor {

    override fun execute(params: List<String>): String {
        val result = DbRequest.getAlcohol(params[0], chatId)
        return if (result == "") {
            "Нет в коллекции"
        } else {
            result
        }
    }
}