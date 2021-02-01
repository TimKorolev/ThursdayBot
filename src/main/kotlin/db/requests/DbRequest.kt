package db.requests

import db.Connections.*
import db.DbHelper
import db.requests.UserRequests.addUser
import db.requests.UserRequests.isUserExist
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


object DbRequest {

    fun getNLastWords(chatId: String, limit: String): String {
        if (!isUserExist(chatId)) {
            addUser(chatId)
        }
        val result =
            DbHelper.getConnection(HerokuDb.url)
                ?.prepareStatement("select word,translate from words where chat_id = '$chatId' order by id desc limit $limit")
                ?.executeQuery()

        var stringResult = ""

        while (result!!.next()) {
            stringResult += result?.getString("word") + " - "
            stringResult += result?.getString("translate") + ",\n"
        }

        return stringResult
    }

    fun getWordsRating(chatId: String, limit: String): String {
        if (!isUserExist(chatId)) {
            addUser(chatId)
        }
        val result =
            DbHelper.getConnection(HerokuDb.url)
                ?.prepareStatement("select word,translate from words where chat_id = '$chatId' order by rating desc limit $limit")
                ?.executeQuery()

        var stringResult = ""

        while (result!!.next()) {
            stringResult += result?.getString("word") + " - "
            stringResult += result?.getString("translate") + ",\n"
        }

        return stringResult.substring(0, stringResult.length - 2)
    }

    fun addWordAndTranslate(word: String, translate: String, chatId: String): String {
        if (!isUserExist(chatId)) {
            addUser(chatId)
        }
        val actualDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        return if (!isWordExist(word, chatId)) {
            DbHelper.getConnection(HerokuDb.url)?.prepareStatement(
                "insert into words(word, translate, chat_id, rating, create_date, last_update_date) values ('$word','$translate','$chatId', 0, '$actualDate', '$actualDate')"
            )?.execute()
            "Запомню $word как '$translate'"
        } else {
            DbHelper.getConnection(HerokuDb.url)?.prepareStatement(
                "update words set last_update_date = '$actualDate' where word = '$word'"
            )?.execute()
            RatingRequests.incrementRating(word, chatId)
            "Слово $word уже встречалось, означает '$translate'"
        }
    }

    fun getNumberOfWord(chatId: String): Double {
        val result =
            DbHelper.getConnection(HerokuDb.url)?.prepareStatement(
                "select count(word) from words where chat_id = '$chatId'"
            )?.executeQuery()

        result!!.next()
        return result.getString("count").toDouble()
    }

    private fun isWordExist(word: String, chatId: String): Boolean {
        var rows = 0
        val result =
            DbHelper.getConnection(HerokuDb.url)
                ?.prepareStatement("select * from words where word = '$word' and chat_id = '$chatId'")
                ?.executeQuery()

        while (result!!.next()) {
            rows++
        }

        return rows > 0
    }

}