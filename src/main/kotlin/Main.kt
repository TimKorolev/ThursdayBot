import bot.Bot
import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import property.Property

fun main() {
    ApiContextInitializer.init()
    val telegramBotsApi = TelegramBotsApi()
    Property()
    try {
        telegramBotsApi.registerBot(Bot())
    } catch (e: TelegramApiRequestException) {
        e.printStackTrace()
    }
}
