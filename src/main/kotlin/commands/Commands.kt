package commands

enum class Commands(_commandName: String) {

    REMIND_ME("Напомни мне"),
    VOCABULARY("Запомни слово"),
    HELP("Помощь"),
    GET_N_LAST_WORDS("Покажи последние");

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