package db.requests

import db.Connections
import db.DbHelper
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit


object UpdateDateRequests {

    fun updateLastUpdateDate(word: String, chatId: String) {
        DbHelper.getConnection(Connections.HerokuDb.url)?.prepareStatement(
            "update words " +
                    "set last_update_date = '${
                        LocalDateTime.now(ZoneId.of("Europe/Moscow")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    }' " +
                    "where (word = '$word' or translate = '$word') " +
                    "and chat_id = '$chatId'"
        )?.execute()
    }

    fun updateUnavailableTo(word: String, chatId: String, now: Boolean = false) {
        DbHelper.getConnection(Connections.HerokuDb.url)?.prepareStatement(
            "update words " +
                    "set unavailable_to = '${
                        if (now) {
                            LocalDateTime.now(ZoneId.of("Europe/Moscow")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        } else {
                            when (RatingRequests.getRating(word, chatId)) {
                                6 -> LocalDateTime.now(ZoneId.of("Europe/Moscow")).plusMonths(1)
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                5 -> LocalDateTime.now(ZoneId.of("Europe/Moscow")).plusWeeks(1)
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                4 -> LocalDateTime.now(ZoneId.of("Europe/Moscow")).plusDays(1)
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                else -> LocalDateTime.now(ZoneId.of("Europe/Moscow")).plusHours(1)
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                            }
                        }
                    }' " +
                    "where (word = '$word' or translate = '$word')" +
                    "and chat_id = '$chatId'"
        )?.execute()
    }
}