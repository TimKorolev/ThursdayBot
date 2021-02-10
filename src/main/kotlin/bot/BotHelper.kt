package bot

import commands.Command
import commands.Commands
import commands.Commands.ADD_WORD

fun getCommandAndParamsFromMessage(message: String): Command {
    val messageArray: List<String> = message.split(" ")

    val command = Commands.getCommandByName(messageArray[0])
    val params = if (command == ADD_WORD) {
        messageArray
    } else {
        messageArray.drop(0)
    }

    return Command(command, params)
}
