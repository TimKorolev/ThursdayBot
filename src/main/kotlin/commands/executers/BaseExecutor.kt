package commands.executers

import commands.ICommandExecutor


abstract class BaseExecutor : ICommandExecutor {

    lateinit var chatId: String

    fun setChatId(_chatId: String): ICommandExecutor {
        chatId = _chatId
        return this
    }
}