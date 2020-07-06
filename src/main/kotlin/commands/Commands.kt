package commands

enum class Commands(_commandName: String) {

    REMIND_ME("Напомни мне"),
    VOCABULARY("Запомни слово"),
    ALCOHOL("Запомни алкоголь"),
    ALCOHOL_RATING("Алкогольный рейтинг"),
    HELP("Помощь"),
    GET_N_LAST_WORDS("Покажи последние"),
    GET_WORDS_RATING("Покажи рейтинг");

    val commandName: String = _commandName

    companion object {
        fun getCommandByName(name: String?): Commands {
            return try {
                values().asSequence().find { command -> command.commandName == name }!!
            } catch (e : Exception){
                HELP
            }
        }
    }
}