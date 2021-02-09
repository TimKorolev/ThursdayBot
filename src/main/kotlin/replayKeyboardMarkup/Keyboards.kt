package replayKeyboardMarkup

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow

object Keyboards {
    fun getStatisticsKeyboard(): ReplyKeyboardMarkup {
        val replyKeyboardMarkup = ReplyKeyboardMarkup()
        replyKeyboardMarkup.selective = true
        replyKeyboardMarkup.resizeKeyboard = true
        replyKeyboardMarkup.oneTimeKeyboard = true

        val keyboard: MutableList<KeyboardRow> = ArrayList()

        val keyboardSecondRow = KeyboardRow()
        keyboardSecondRow.add(KeyboardButton("timeVsNewWords"))

        keyboard.add(keyboardSecondRow)
        replyKeyboardMarkup.keyboard = keyboard
        return replyKeyboardMarkup
    }

    fun getDefaultKeyboard(): ReplyKeyboardMarkup {
        val replyKeyboardMarkup = ReplyKeyboardMarkup()
        replyKeyboardMarkup.selective = true
        replyKeyboardMarkup.resizeKeyboard = true
        replyKeyboardMarkup.oneTimeKeyboard = false

        val keyboard: MutableList<KeyboardRow> = java.util.ArrayList()

        val keyboardSecondRow = KeyboardRow()
        keyboardSecondRow.add(KeyboardButton("startPoll"))
        keyboardSecondRow.add(KeyboardButton("help"))

        keyboard.add(keyboardSecondRow)
        replyKeyboardMarkup.keyboard = keyboard
        return replyKeyboardMarkup
    }
}