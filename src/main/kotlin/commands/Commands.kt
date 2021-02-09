package commands

enum class Commands(_commandName: String) {

    START_POLL("startPoll"),
    ADD_WORD("addWord"),
    STATISTICS("statistics"),
    HELP("help");

    val commandName: String = _commandName

    companion object {
        fun getCommandByName(name: String?): Commands {
            return try {
                values().asSequence().find { command -> command.commandName.equals(name, true) }!!
            } catch (e: Exception) {
                ADD_WORD
            }
        }
    }
}