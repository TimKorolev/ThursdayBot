package bot

import commands.Commands.*
import commands.executers.BaseExecutor
import commands.executers.HelpExecutor
import commands.executers.RemindMeExecutor
import commands.executers.words.GetNLastWordsExecutor
import commands.executers.words.GetWordsRatingExecutor
import commands.executers.words.VocabularyExecutor
import db.requests.RatingRequests.decrementRating
import db.requests.RatingRequests.incrementRating
import db.requests.StudyRequests.getStudyWord
import db.requests.StudyRequests.getStudyWords
import db.requests.UserRequests.incrementUserRating
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.util.*
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

            return
        }

        val chatId = update.message.chatId.toString()
        val message = update.message.text
        val command = getCommandAndParamsFromMessage(message)

        when (command.command) {
            REMIND_ME -> sendMsg(chatId, RemindMeExecutor, command.params)
            VOCABULARY -> sendMsg(chatId, VocabularyExecutor, command.params)
            GET_N_LAST_WORDS -> sendMsg(chatId, GetNLastWordsExecutor, command.params)
            GET_WORDS_RATING -> sendMsg(chatId, GetWordsRatingExecutor, command.params)
            START_QUESTIONNAIRE -> {
                var i = 5
                while (i != 0){
                    sendPoll(chatId)
                i--}
            }
            else -> sendMsg(chatId, HelpExecutor, command.params)
        }
    }

    @Synchronized
    fun sendMsg(chatId: String, executor: BaseExecutor?, commandProperties: List<String>) {
        val text = executor?.setChatId(chatId)?.execute(commandProperties)

        val sendMessage = SendMessage()
        sendMessage.enableMarkdown(true)
        sendMessage.chatId = chatId
        sendMessage.text = text
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

        val rightAnswer = Random.nextInt(0, studyWords.size)

        studyWords.add(rightAnswer, studyWord)

        val sendPoll = SendPoll()
        sendPoll.chatId = chatId
        sendPoll.question = "Translate the word '${studyWord.word}'"
        sendPoll.options = studyWords.map { studyWord -> studyWord.translate }
        sendPoll.type = "quiz"
        sendPoll.correctOptionId = rightAnswer
        sendPoll.openPeriod = 60
        sendPoll.replyMarkup

        BotContext.addPoll(sendPoll)

//        val inlineKeyboardMarkup = InlineKeyboardMarkup()
//
//        val keyboard = mutableListOf<InlineKeyboardButton>()
//        studyWords.map { studyWord -> studyWord.translate }.forEach { option ->
//            var inlineKeyboardButton = InlineKeyboardButton(option)
//            inlineKeyboardButton.callbackData = option
//            keyboard.add(inlineKeyboardButton)
//        }
//        inlineKeyboardMarkup.keyboard = listOf(keyboard.toList())
//
//        sendPoll.replyMarkup = inlineKeyboardMarkup
        try {
            execute(sendPoll)
        } catch (e: TelegramApiException) {
            e.toString()
        }
    }

    @Synchronized
    fun setButtons(sendMessage: SendMessage) {
        val replyKeyboardMarkup = ReplyKeyboardMarkup()
        sendMessage.replyMarkup = replyKeyboardMarkup
        replyKeyboardMarkup.selective = true
        replyKeyboardMarkup.resizeKeyboard = true
        replyKeyboardMarkup.oneTimeKeyboard = false

        // Создаем список строк клавиатуры
        val keyboard: MutableList<KeyboardRow> = ArrayList()

        // Первая строчка клавиатуры
        val keyboardFirstRow = KeyboardRow()
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add(KeyboardButton("Покажи последние"))
        keyboardFirstRow.add(KeyboardButton("Покажи последние"))

        // Вторая строчка клавиатуры
        val keyboardSecondRow = KeyboardRow()
        // Добавляем кнопки во вторую строчку клавиатуры
        keyboardSecondRow.add(KeyboardButton("Помощь"))

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow)
        keyboard.add(keyboardSecondRow)
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.keyboard = keyboard
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