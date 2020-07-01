package commands.executers

import commands.ICommandExecuter
import java.sql.Time
import java.time.LocalDate

object VocabularyExecuter : ICommandExecuter {

    override fun execute(params: List<String>): String {
        lateinit var date: String
        var time = Time.valueOf("09:00:00")
        lateinit var data: String

        when (val remindDate = params[0]) {
            "сегодня" -> date = LocalDate.now().toString()
            "завтра" -> date = LocalDate.now().plusDays(1).toString()
            else -> date = LocalDate.parse(remindDate).toString()
        }

        if (params[1] == "в") {
            time = Time.valueOf(params[2] + ":00")
            data = params.drop(3).toString().replace(",", "")
        }

        return "Напомню $date в ${time.toString().dropLast(3)} $data"
    }
}