package bot

import commands.Commands
import commands.Commands.*
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.util.*

class Bot : TelegramLongPollingBot() {
    /**
     * Метод для приема сообщений.
     *
     * @param update Содержит сообщение от пользователя.
     */
    override fun onUpdateReceived(update: Update) {
        val message = update.message.text
        val messageArray : List<String> = message.split(" ")
        val command = messageArray[0] + " " + messageArray[1]
        var text = ""

        when (command) {
            REMIND_ME.getCommandName() -> text = "Напомню"
            VOCABULARY.getCommandName() -> text = "Запомнил"
        }
        sendMsg(update.message.chatId.toString(), text)
    }

    /**
     * Метод для настройки сообщения и его отправки.
     *
     * @param chatId id чата
     * @param s      Строка, которую необходимот отправить в качестве сообщения.
     */
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
        // Создаем клавиуатуру
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
        keyboardFirstRow.add(KeyboardButton("Привет"))

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