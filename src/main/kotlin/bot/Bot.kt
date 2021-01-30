package bot

import commands.Commands.*
import commands.executers.HelpExecutor
import commands.executers.RemindMeExecutor
import commands.executers.alcohol.AlcoholExecutor
import commands.executers.alcohol.AlcoholRatingExecutor
import commands.executers.alcohol.GetAlcoholExecutor
import commands.executers.words.GetNLastWordsExecutor
import commands.executers.words.GetWordsRatingExecutor
import commands.executers.words.VocabularyExecutor
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.util.*

class Bot : TelegramLongPollingBot() {

    override fun onUpdateReceived(update: Update) {
        val message = update.message.text
        val command = getCommandAndParamsFromMessage(message)
        val chatId = update.message.chatId.toString()

        sendMsg(
            chatId, when (command.command) {
                REMIND_ME -> RemindMeExecutor
                VOCABULARY -> VocabularyExecutor
                GET_N_LAST_WORDS -> GetNLastWordsExecutor
                GET_WORDS_RATING -> GetWordsRatingExecutor
                ALCOHOL -> AlcoholExecutor
                GET_ALCOHOL -> GetAlcoholExecutor
                ALCOHOL_RATING -> AlcoholRatingExecutor
                HELP -> HelpExecutor
                else -> HelpExecutor
            }.setChatId(chatId).execute(command.params)
        )
    }

    @Synchronized
    fun sendMsg(chatId: String?, s: String?) {
        val sendMessage = SendMessage()
        sendMessage.enableMarkdown(true)
        sendMessage.chatId = chatId
        sendMessage.text = s
        setButtons(sendMessage)
        try {
            execute(sendMessage)
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