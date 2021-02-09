package db.requests

import db.Connections
import db.DbHelper
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit


object UpdateDateRequests {

    fun updateLastUpdateDate(word: String, chatId: String) {
        DbHelper.getConnection(Connections.HerokuDb.url)?.prepareStatement(
            "update words " +
                    "set last_update_date = '${
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
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
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        } else {
                            when (RatingRequests.getRating(word, chatId)) {
                                6 -> LocalDateTime.now().plusMonths(1)
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                5 -> LocalDateTime.now().plusWeeks(1)
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                else -> LocalDateTime.now().plusDays(1)
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                            }
                        }
                    }' " +
                    "where (word = '$word' or translate = '$word')" +
                    "and chat_id = '$chatId'"
        )?.execute()
    }
}