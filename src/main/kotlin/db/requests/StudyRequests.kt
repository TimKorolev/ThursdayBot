package db.requests

import db.Connections
import db.DbHelper
import db.entities.StudyWord
import db.requests.UserRequests.addUser
import db.requests.UserRequests.isUserExist
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.ceil
import kotlin.random.Random

object StudyRequests {

    fun getStudyWord(chatId: String): StudyWord {
        if (!isUserExist(chatId)) {
            addUser(chatId)
        }

        val selectionSize = ceil(WordsRequest.getNumberOfWord(chatId) * 0.1).toInt() + 1
        val result =
            DbHelper.getConnection(Connections.HerokuDb.url)
                ?.prepareStatement(
                    "select word,translate from words where chat_id = '$chatId' and unavailable_to < '${
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    }' order by rating limit $selectionSize"
                )
                ?.executeQuery()

        val words = mutableListOf<StudyWord>()

        while (result!!.next()) {
            words.add(
                StudyWord(
                    result.getString("word"),
                    result.getString("translate")
                )
            )
        }
        return words[Random.nextInt(0, words.size - 1)]
    }

    fun getStudyWords(chatId: String): MutableList<StudyWord> {
        if (!isUserExist(chatId)) {
            addUser(chatId)
        }

        val result =
            DbHelper.getConnection(Connections.HerokuDb.url)
                ?.prepareStatement(
                    "select word,translate from words where chat_id = '$chatId' and unavailable_to < '${
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    }' order by random() limit 4"
                ) // poll size
                ?.executeQuery()

        val words = mutableListOf<StudyWord>()

            while (result!!.next()) {
                words.add(
                    StudyWord(
                        result.getString("word"),
                        result.getString("translate")
                    )
                )
            }

        return words
    }
}