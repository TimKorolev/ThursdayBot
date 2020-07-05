package bot

import commands.Command
import commands.Commands

fun getCommandAndParamsFromMessage(message: String): Command {
    val messageArray: List<String> = message.split(" ")
    lateinit var command: String
    command = try {
        messageArray[0] + " " + messageArray[1]
    } catch (e: IndexOutOfBoundsException) {
        messageArray[0]
    }
    return Command(Commands.getCommandByName(command), messageArray.drop(2))
}
