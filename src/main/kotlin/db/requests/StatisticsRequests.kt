package db.requests

import db.Connections
import db.DbHelper

object StatisticsRequests {
    fun getTimeVsNewWords(chatId: String): Map<MutableList<String>, MutableList<String>> {
        val result = DbHelper.getConnection(Connections.HerokuDb.url)?.prepareStatement(
            "select word, create_date from words where (chat_id = '$chatId')"
        )?.executeQuery()

        val dataX = mutableListOf<String>()
        val dataY = mutableListOf<String>()

        while (result!!.next()) {
            dataX.add(result.getString("word"))
            dataY.add(result.getString("create_date"))
        }

        return mapOf(Pair(dataX,dataY))
    }
}