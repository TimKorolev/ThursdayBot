package db

import java.sql.Connection
import java.sql.DriverManager


object DbHelper {

    lateinit var connection: Connection

    fun getConnection(url: String): Connection? {
        try {
            Class.forName("org.postgresql.Driver")
            connection = DriverManager.getConnection(url)
            return connection
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}