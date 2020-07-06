package commands.executers

import commands.ICommandExecuter
import db.DbRequest


object HelpExecuter : ICommandExecuter {

    override lateinit var chatId: String

    fun setChatId(_chatId: String): HelpExecuter {
        chatId = _chatId
        return this
    }

    override fun execute(params: List<String>): String {

        return """Команды:
. Запомни слово <word> - запоминает английское слово с переводом
. Покажи последние <limit> - показывает список из <limit> слов с переводом (по умолчанию 10)
. Покажи рейтинг <limit> - показывает <limit> наиболее проблемных слов с переводом (по умолчанию 10)
. Запомни алкоголь <name> <rating> <description> - запомнит алкоголь с рейтингом и описание
. Алкогольный рейтинг - показывает рейтинг алкоголя
"""
    }

}