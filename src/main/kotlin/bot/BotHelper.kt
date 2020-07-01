package bot

import commands.Command
import commands.Commands

fun getCommandAndParamsFromMessage(message: String): Command {
    val messageArray: List<String> = message.split(" ")
    val command = messageArray[0] + " " + messageArray[1]
    messageArray.drop(2)
    return Command(Commands.getCommandByName(command), messageArray)
}
