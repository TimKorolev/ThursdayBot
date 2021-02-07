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
                    "set last_update_date = '${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}' " +
                    "where (word = '$word' or translate = '$word') " +
                    "and chat_id = '$chatId'"
        )?.execute()
    }

    fun updateUnavailableTo(word: String, chatId: String) {
        DbHelper.getConnection(Connections.HerokuDb.url)?.prepareStatement(
            "update words " +
                    "set unavailable_to = '${LocalDateTime.now().plusHours(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}' " +
                    "where (word = '$word' or translate = '$word')" +
                    "and chat_id = '$chatId'"
        )?.execute()
    }
}