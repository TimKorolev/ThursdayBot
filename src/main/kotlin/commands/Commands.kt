package commands

enum class Commands(_commandName: String) {

    REMIND_ME("Напомни мне"),
    VOCABULARY("Запомни слово");

    val commandName: String = _commandName

    companion object {
        fun getCommandByName(name: String?): Commands {
            return values().asSequence().find { command -> command.commandName == name }!!
        }
    }
}