package bot

import bot.BotContext.deletePollCounter
import bot.BotContext.incrementPollCounter
import commands.Commands.*
import commands.executers.BaseExecutor
import commands.executers.HelpExecutor
import commands.executers.words.AddWord
import commands.replayKeyboardMarkup.Keyboards.getDefaultKeyboard
import db.requests.RatingRequests.decrementRating
import db.requests.RatingRequests.incrementRating
import db.requests.StudyRequests.getStudyWord
import db.requests.StudyRequests.getStudyWords
import db.requests.UserRequests.incrementUserRating
import db.requests.WordsRequest.addWordFromWord10000
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import kotlin.random.Random

class Bot : TelegramLongPollingBot() {

    override fun onUpdateReceived(update: Update) {

        if (!update.hasMessage() && update.hasPoll()) {
            val chatId = BotContext.getPollChatId(update)

            if (update.poll.options[update.poll.correctOptionId].voterCount == 1) {
                incrementRating(update.poll.options[update.poll.correctOptionId].text, chatId)
                incrementUserRating(chatId)
            } else {
                decrementRating(update.poll.options[update.poll.correctOptionId].text, chatId)
                decrementRating(update.poll.options.filter { option -> option.voterCount == 1 }[0].text, chatId)
            }
            if (BotContext.getPollCounter(chatId) < 10) {
                incrementPollCounter(chatId)
                sendPoll(chatId)
            } else {
                sendMsg(chatId, _text = "Poll is over")
                val addedWords = addWordFromWord10000(chatId)
                if(addedWords.isNotEmpty()){
                    sendMsg(chatId, _text = "New words: \n " + addedWords.toString()
                        .replace(",",",\n")
                        .replace("The word", ""))
                }
                deletePollCounter(chatId)
            }
            return
        }

        val chatId = update.message.chatId.toString()
        val message = update.message.text
        val command = getCommandAndParamsFromMessage(message)

        when (command.command) {
            HELP -> sendMsg(chatId, HelpExecutor, command.params)
            START_POLL -> {
                BotContext.addPollCounter(chatId)
                sendPoll(chatId)
            }
//            STATISTICS -> sendMsg(chatId, _text = "Сhoose parameters", replyKeyboardMarkup = getStatisticsKeyboard())
            ADD_WORD -> sendMsg(chatId, AddWord, command.params)
        }
    }

    @Synchronized
    fun sendMsg(
        chatId: String,
        executor: BaseExecutor? = null,
        commandProperties: List<String> = listOf(),
        _text: String? = null,
        replyKeyboardMarkup: ReplyKeyboardMarkup? = null
    ) {
        val text = _text ?: executor?.setChatId(chatId)?.execute(commandProperties)

        val sendMessage = SendMessage()
        sendMessage.enableMarkdown(true)
        sendMessage.chatId = chatId
        sendMessage.text = text
        if (replyKeyboardMarkup != null) sendMessage.replyMarkup = replyKeyboardMarkup
        setButtons(sendMessage)
        try {
            execute(sendMessage)
        } catch (e: TelegramApiException) {
            e.toString()
        }
    }

    @Synchronized
    fun sendPoll(chatId: String) {
        val studyWord = getStudyWord(chatId)
        var studyWords = getStudyWords(chatId)

        while (studyWord in studyWords) {
            studyWords = getStudyWords(chatId)
        }

        val rightAnswer = Random.nextInt(0, studyWords.size + 1)

        studyWords.add(rightAnswer, studyWord)

        val sendPoll = SendPoll()
        sendPoll.chatId = chatId
        sendPoll.question = "Translate the word '${studyWord.word}'"
        sendPoll.options = studyWords.map { studyWord -> studyWord.translate }
        sendPoll.type = "quiz"
        sendPoll.correctOptionId = rightAnswer
        sendPoll.openPeriod = 15
        sendPoll.replyMarkup

        BotContext.addPoll(sendPoll)

        try {
            execute(sendPoll)
        } catch (e: TelegramApiException) {
            e.toString()
        }
    }

    @Synchronized
    fun setButtons(sendMessage: SendMessage) {
        if (sendMessage.replyMarkup == null) {
            sendMessage.replyMarkup = getDefaultKeyboard()
        }
    }

    /**
     * Метод возвращает имя бота, указанное при регистрации.
     *
     * @return имя бота
     */
    override fun getBotUsername(): String {
        return "Thursday"
    }

    /**
     * Метод возвращает token бота для связи с сервером Telegram
     *
     * @return token для бота
     */
    override fun getBotToken(): String {
        return "1127730685:AAGIZ180_fCo9Ffm8nkW93pQI60LyUa3dfU"
    }
}