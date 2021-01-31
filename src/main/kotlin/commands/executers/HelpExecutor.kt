package commands.executers

import commands.ICommandExecutor


object HelpExecutor : BaseExecutor(), ICommandExecutor {

    override fun execute(params: List<String>): String {

        return """Команды:
. Запомни слово <word> - запоминает английское слово с переводом
. Покажи последние <limit> - показывает список из <limit> слов с переводом (по умолчанию 10)
. Покажи рейтинг <limit> - показывает <limit> наиболее проблемных слов с переводом (по умолчанию 10)
. Запомни алкоголь <name> <rating> <description> - запомнит алкоголь с рейтингом и описание
. Алкогольный рейтинг - показывает рейтинг алкоголя
. Найди алкоголь <name> - показывает <name>, рейтинг и описание алкоголя, если есть в коллекции
"""
    }

}