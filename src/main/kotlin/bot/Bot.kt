package bot

import commands.Commands.*
import commands.executers.BaseExecutor
import commands.executers.HelpExecutor
import commands.executers.RemindMeExecutor
import commands.executers.alcohol.AlcoholExecutor
import commands.executers.alcohol.AlcoholRatingExecutor
import commands.executers.alcohol.GetAlcoholExecutor
import commands.executers.words.GetNLastWordsExecutor
import commands.executers.words.GetWordsRatingExecutor
import commands.executers.words.VocabularyExecutor
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.util.*

class Bot : TelegramLongPollingBot() {

    override fun onUpdateReceived(update: Update) {

        if (!update.hasMessage() && update.hasPoll()) {
            if (update.pollAnswer == null) {
                print("fooooo")
            }
        }

        val chatId = update.message.chatId.toString()
        val message = update.message.text
        val command = getCommandAndParamsFromMessage(message)

        when (command.command) {
            REMIND_ME -> sendMsg(chatId, RemindMeExecutor, command.params)
            VOCABULARY -> sendMsg(chatId, VocabularyExecutor, command.params)
            GET_N_LAST_WORDS -> sendMsg(chatId, GetNLastWordsExecutor, command.params)
            GET_WORDS_RATING -> sendMsg(chatId, GetWordsRatingExecutor, command.params)
            ALCOHOL -> sendMsg(chatId, AlcoholExecutor, command.params)
            GET_ALCOHOL -> sendMsg(chatId, GetAlcoholExecutor, command.params)
            START_QUESTIONNAIRE -> sendPoll(chatId)
            ALCOHOL_RATING -> sendMsg(chatId, AlcoholRatingExecutor, command.params)
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
        val word = "Let"
        val options = listOf("быть", "пусть", "грусть")
        val sendPoll = SendPoll()
        sendPoll.chatId = chatId
        sendPoll.question = "Translate the word '${word}'"
        sendPoll.options = options
        sendPoll.type = "quiz"
        sendPoll.correctOptionId = 2
        sendPoll.openPeriod = 60

//        val inlineKeyboardMarkup = InlineKeyboardMarkup()
//
//        val keyboard = mutableListOf<InlineKeyboardButton>()
//        options.forEach { option ->
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