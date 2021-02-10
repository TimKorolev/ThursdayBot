package db.requests

import com.opencsv.CSVReader
import constant.UserRatingDependency
import db.Connections.HerokuDb
import db.DbHelper
import db.entities.StudyWord
import db.requests.RatingRequests.decrementRating
import db.requests.UpdateDateRequests.updateLastUpdateDate
import db.requests.UpdateDateRequests.updateUnavailableTo
import db.requests.UserRequests.addUser
import db.requests.UserRequests.getUserRating
import db.requests.UserRequests.isUserExist
import java.io.FileReader
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


object WordsRequest {

    fun addWordAndTranslate(_word: String, _translate: String, chatId: String): String {

        val word = _word.toLowerCase()
        val translate = _translate.toLowerCase()

        if (!isUserExist(chatId)) {
            addUser(chatId)
        }
        val actualDate = LocalDateTime.now(ZoneId.of("Europe/Moscow")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        return if (!isWordExist(word, chatId)) {
            DbHelper.getConnection(HerokuDb.url)?.prepareStatement(
                "insert into words(word, translate, chat_id, rating, create_date, last_update_date) values ('$word','$translate','$chatId', 0, '$actualDate', '$actualDate')"
            )?.execute()
            updateUnavailableTo(word, chatId, true)
            "Add word '$word' as '$translate'"
        } else {
            updateLastUpdateDate(word, chatId)
            decrementRating(word, chatId)
            updateUnavailableTo(word, chatId)
            "The word '$word' means '$translate'"
        }
    }

    fun deleteWord(_word: String, chatId: String): String {

        val word = _word.toLowerCase()

        if (!isUserExist(chatId)) {
            addUser(chatId)
        }

        return if (isWordExist(word, chatId)) {
            DbHelper.getConnection(HerokuDb.url)?.prepareStatement(
                "delete from words where word = '$word' and chat_id = '$chatId'"
            )?.execute()
            "The word '$word' was deleted"
        } else {
            "The word '$word' not found"
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

    private fun isWordExist(_word: String, chatId: String): Boolean {
        val word = _word.toLowerCase()
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

    fun addWordFromWord10000(chatId: String): MutableList<String> {
        val fileReader = FileReader("src/main/resources/word_10000.csv")
        val csvReader = CSVReader(fileReader)
        val userLevel = UserRatingDependency
            .values()
            .findLast { level -> level.rating < getUserRating(chatId) }
            .toString()
            .split("_")[1]
            .toInt() - 1
        csvReader.skip(userLevel * 9)

        val wordTranslateList = mutableListOf<StudyWord>()

        var i = 0
        while (i < 11) {
            val line = csvReader.readNext()
            wordTranslateList.add(
                StudyWord(line[0], line[1])
            )
            i++
        }

        val addedWords = mutableListOf<String>()

        wordTranslateList.forEach { wordTranslate ->
            addedWords.add(
                addWordAndTranslate(
                    wordTranslate.word,
                    wordTranslate.translate,
                    chatId
                )
            )
        }
        return addedWords.filterNot { string -> string.contains("means") }.toMutableList()
    }
}