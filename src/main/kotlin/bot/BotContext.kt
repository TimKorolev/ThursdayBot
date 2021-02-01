package bot

import org.telegram.telegrambots.meta.api.methods.polls.SendPoll
import org.telegram.telegrambots.meta.api.objects.Update

object BotContext {
    private val polls = mutableListOf<SendPoll>()

    fun getPollChatId(update: Update): String {
        val chatId: String = polls.filter { poll -> poll.question == update.poll.question }[0].chatId
        polls.removeIf { poll -> poll.question == update.poll.question }
        return chatId
    }

    fun addPoll(poll: SendPoll) {
        polls.add(poll)
    }
}