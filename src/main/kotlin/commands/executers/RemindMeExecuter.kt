package commands.executers

import commands.ICommandExecuter
import java.time.LocalDate

object RemindMeExecuter : ICommandExecuter{
    lateinit var date : LocalDate

    override fun execute(params: List<String>): Any {
        val remindDate = params[0]
        return when(remindDate){
            "сегодня" -> LocalDate.now()
            "завтра" -> LocalDate.now().plusDays(1)
            else -> LocalDate.parse(remindDate)
        }
    }

}