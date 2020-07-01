package commands

enum class Commands(
    private val commandName : String) {

    REMIND_ME("Напомни мне"),
    VOCABULARY("Запомни слово");

    fun getCommandName(): String {
        return commandName;
    }

}