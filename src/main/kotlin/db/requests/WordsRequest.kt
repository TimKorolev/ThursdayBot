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
import java.time.format.DateTimeFormatter


object WordsRequest {

    fun addWordAndTranslate(word: String, translate: String, chatId: String): String {
        if (!isUserExist(chatId)) {
            addUser(chatId)
        }
        val actualDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
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
        while (i < 10) {
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

        return addedWords.filter { string -> string.contains("means") }.toMutableList()
    }
}