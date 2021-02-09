package db.requests

import db.Connections
import db.DbHelper
import db.requests.UpdateDateRequests.updateLastUpdateDate
import db.requests.UpdateDateRequests.updateUnavailableTo

object RatingRequests {
    fun incrementRating(word: String, chatId: String) {
        var rating = getRating(word, chatId)

        if (rating > 8) {
            return
        }

        rating++

        DbHelper.getConnection(Connections.HerokuDb.url)?.prepareStatement(
            "update words set rating = '$rating' where (word = '$word' or translate = '$word')"
        )?.execute()

        updateLastUpdateDate(word,chatId)
        updateUnavailableTo(word,chatId)
    }

    fun decrementRating(word: String, chatId: String) {
        var rating = getRating(word, chatId)

        if (rating == 0) {
            return
        }

        rating--

        DbHelper.getConnection(Connections.HerokuDb.url)?.prepareStatement(
            "update words set rating = '$rating' where (word = '$word' or translate = '$word')"
        )?.execute()

        updateLastUpdateDate(word,chatId)
        updateUnavailableTo(word,chatId)
    }

    fun getRating(word: String, chatId: String): Int {
        val result = DbHelper.getConnection(Connections.HerokuDb.url)?.prepareStatement(
            "select rating from words where (word = '$word' or translate = '$word') and chat_id = '$chatId'"
        )?.executeQuery()

        result!!.next()
        return result.getString("rating").toInt()
    }
}