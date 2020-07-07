package bot

import commands.Commands.*
import commands.executers.*
import commands.executers.alcohol.AlcoholExecuter
import commands.executers.alcohol.AlcoholRatingExecuter
import commands.executers.alcohol.GetAlcoholExecuter
import commands.executers.words.GetNLastWordsExecuter
import commands.executers.words.GetWordsRatingExecuter
import commands.executers.words.VocabularyExecuter
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
        lateinit var text: String
        val chatId = update.message.chatId.toString()

        when (command.command) {
            REMIND_ME -> {
                sendMsg(chatId, RemindMeExecuter.setChatId(chatId).execute(command.params))
            }
            VOCABULARY -> {
                sendMsg(chatId, VocabularyExecuter.setChatId(chatId).execute(command.params))
            }
            GET_N_LAST_WORDS -> {
                sendMsg(chatId, GetNLastWordsExecuter.setChatId(chatId).execute(command.params))
            }
            GET_WORDS_RATING -> {
                sendMsg(chatId, GetWordsRatingExecuter.setChatId(chatId).execute(command.params))
            }
            ALCOHOL -> {
                sendMsg(chatId, AlcoholExecuter.setChatId(chatId).execute(command.params))
            }
            GET_ALCOHOL -> {
                sendMsg(chatId, GetAlcoholExecuter.setChatId(chatId).execute(command.params))
            }
            ALCOHOL_RATING -> {
                sendMsg(chatId, AlcoholRatingExecuter.setChatId(chatId).execute(command.params))
            }
            HELP -> {
                sendMsg(chatId, HelpExecuter.setChatId(chatId).execute(command.params))
            }
        }
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