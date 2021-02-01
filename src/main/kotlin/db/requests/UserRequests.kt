package db.requests

import db.Connections
import db.DbHelper
import db.requests.UpdateDateRequests.updateLastUpdateDate


object UserRequests {

    fun incrementUserRating(chatId: String) {
        var rating = getUserRating(chatId)
        rating++

        DbHelper.getConnection(Connections.HerokuDb.url)?.prepareStatement(
            "update users set rating = '$rating' where chat_Id = '$chatId'"
        )?.execute()

    }

    fun getUserRating(chatId: String): Int {
        val result = DbHelper.getConnection(Connections.HerokuDb.url)?.prepareStatement(
            "select rating from users where chat_id = '$chatId'"
        )?.executeQuery()

        result!!.next()
        return result.getString("rating").toInt()
    }

    fun addUser(chatId: String) {
        DbHelper.getConnection(Connections.HerokuDb.url)?.prepareStatement(
            "insert into users(chat_id) values ('$chatId')"
        )?.execute()
    }

    fun isUserExist(chatId: String): Boolean {
        var rows = 0
        val result =
            DbHelper.getConnection(Connections.HerokuDb.url)
                ?.prepareStatement("select * from users where chat_id = '$chatId'")
                ?.executeQuery()

        while (result!!.next()) {
            rows++
        }

        return rows > 0
    }
}