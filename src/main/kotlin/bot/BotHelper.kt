package bot

import commands.Command
import commands.Commands

fun getCommandAndParamsFromMessage(message: String): Command {
    val messageArray: List<String> = message.split(" ")
    val command: String = messageArray[0]
    return Command(Commands.getCommandByName(command), messageArray.drop(1))
}
