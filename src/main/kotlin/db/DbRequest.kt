package db

import db.Connections.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


object DbRequest {

    fun addAlcohol(name: String, rating: Int, description: String, chatId: String): String {
        if (!isUserExist(chatId)) {
            addUser(chatId)
        }

        return if (!isAlcoholExist(name, chatId)) {
            DbHelper.getConnection(HerokuDb.url)?.prepareStatement(
                "insert into alcohol(name, rating, description, chat_id) values ('$name','$rating','$description','$chatId')"
            )?.execute()
            "Запомню $name"
        } else {
            "$name уже коллекции"
        }
    }

    fun getAlcoholRating(chatId: String): String {
        val result = DbHelper.getConnection(HerokuDb.url)?.prepareStatement(
            "select name, rating from alcohol where chat_id = '$chatId' order by rating desc"
        )?.executeQuery()

        var stringResult = ""

        while (result!!.next()) {
            stringResult += result?.getString("name") + " - "
            stringResult += result?.getString("rating") + ",\n"
        }

        return stringResult.substring(0, stringResult.length - 2)
    }

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
        val actualDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE).replace("-", ".")
        return if (!isWordExist(word, chatId)) {
            DbHelper.getConnection(HerokuDb.url)?.prepareStatement(
                "insert into words(word, translate, chat_id, rating, create_date, last_update_date) values ('$word','$translate','$chatId', 0, '$actualDate', '$actualDate')"
            )?.execute()
            "Запомню $word как '$translate'"
        } else {
            DbHelper.getConnection(HerokuDb.url)?.prepareStatement(
                "update words set last_update_date = '$actualDate' where word = '$word'"
            )?.execute()
            incrementRating(word, chatId)
            "Слово $word уже встречалось, означает '$translate'"
        }
    }

    private fun addUser(chatId: String) {
        DbHelper.getConnection(HerokuDb.url)?.prepareStatement(
            "insert into users(chat_id) values ('$chatId')"
        )?.execute()
    }

    private fun isUserExist(chatId: String): Boolean {
        var rows = 0
        val result =
            DbHelper.getConnection(HerokuDb.url)
                ?.prepareStatement("select * from users where chat_id = '$chatId'")
                ?.executeQuery()

        while (result!!.next()) {
            rows++
        }

        return rows > 0
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

    private fun isAlcoholExist(name: String, chatId: String): Boolean {
        var rows = 0
        val result =
            DbHelper.getConnection(HerokuDb.url)
                ?.prepareStatement("select * from alcohol where name = '$name' and chat_id = '$chatId'")
                ?.executeQuery()

        while (result!!.next()) {
            rows++
        }

        return rows > 0
    }

    private fun incrementRating(word: String, chatId: String) {
        var rating = getRating(word, chatId)
        var intRating = rating.toInt()

        intRating++
        rating = intRating.toString()

        DbHelper.getConnection(HerokuDb.url)?.prepareStatement(
            "update words set rating = '$rating' where word = '$word'"
        )?.execute()
    }

    private fun decrementRating(word: String, chatId: String) {
        var rating = getRating(word, chatId)
        var intRating = rating.toInt()

        if (intRating == 0) {
            return
        }

        intRating--
        rating = intRating.toString()

        DbHelper.getConnection(HerokuDb.url)?.prepareStatement(
            "update words set rating = '$rating' where word = '$word'"
        )?.execute()
    }

    private fun getRating(word: String, chatId: String): String {
        val result = DbHelper.getConnection(HerokuDb.url)?.prepareStatement(
            "select rating from words where word = '$word' and chat_id = '$chatId'"
        )?.executeQuery()

        result!!.next()
        return result.getString("rating").toString()
    }

    fun addAlcohol(name: String, rating: Int, description: String) {

    }

}