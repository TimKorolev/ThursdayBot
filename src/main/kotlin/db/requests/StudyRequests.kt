package db.requests

import db.Connections
import db.DbHelper
import db.entities.StudyWord
import db.requests.UserRequests.addUser
import db.requests.UserRequests.isUserExist
import kotlin.math.ceil
import kotlin.random.Random

object StudyRequests {

    fun getStudyWord(chatId: String): StudyWord {
        if (!isUserExist(chatId)) {
            addUser(chatId)
        }

        val selectionSize = ceil(DbRequest.getNumberOfWord(chatId) * 0.1).toInt() + 1
        val result =
            DbHelper.getConnection(Connections.HerokuDb.url)
                ?.prepareStatement("select word,translate from words where chat_id = '$chatId' order by rating limit $selectionSize")
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
                ?.prepareStatement("select word,translate from words where chat_id = '$chatId' order by random() limit 3")
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