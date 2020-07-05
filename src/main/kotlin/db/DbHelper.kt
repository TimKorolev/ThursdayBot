package db

import java.sql.Connection
import java.sql.DriverManager


object DbHelper {

    lateinit var connection: Connection
    var connectionPool: HashMap<String, Connection> = HashMap()

    fun getConnection(url: String): Connection? {
        try {
            if (connectionPool[url] == null) {
                Class.forName("org.postgresql.Driver")
                connection = DriverManager.getConnection(url)
                connectionPool[url] = connection
            }
            connection = connectionPool[url]!!
            return connection
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}