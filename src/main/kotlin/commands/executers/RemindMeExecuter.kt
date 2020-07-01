package commands.executers

import commands.ICommandExecuter
import java.time.LocalDate

object RemindMeExecuter : ICommandExecuter{
    lateinit var date : LocalDate

    override fun execute(params: List<String>): String {
        return when(val remindDate = params[0]){
            "сегодня" -> LocalDate.now().toString()
            "завтра" -> LocalDate.now().plusDays(1).toString()
            else -> LocalDate.parse(remindDate).toString()
        }
    }

}