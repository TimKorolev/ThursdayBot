package bot

import org.telegram.telegrambots.meta.api.methods.polls.SendPoll
import org.telegram.telegrambots.meta.api.objects.Update

object BotContext {
    private val polls = mutableListOf<SendPoll>()
    private val pollPerChatCounter = mutableMapOf<String, Int>()

    fun getPollChatId(update: Update): String {
        val chatId: String = polls.filter { poll -> poll.question == update.poll.question }[0].chatId
        polls.removeIf { poll -> poll.question == update.poll.question }
        return chatId
    }

    fun addPoll(poll: SendPoll) {
        polls.add(poll)
    }

    fun incrementPollCounter(chatId: String) {
        val pollNumber = pollPerChatCounter[chatId]!!
        pollPerChatCounter[chatId] = pollNumber + 1
    }

    fun getPollCounter(chatId: String): Int {
        return pollPerChatCounter.getOrDefault(chatId, 0)
    }

    fun addPollCounter(chatId: String) {
        pollPerChatCounter[chatId] = 0
    }

    fun deletePollCounter(chatId: String) {
        pollPerChatCounter.remove(chatId)
    }

}