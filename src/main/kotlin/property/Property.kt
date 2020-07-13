package property

import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*


class Property {
    companion object {
        lateinit var TRANSLATOR_URL: String
        lateinit var TRANSLATOR_KEY: String

        lateinit var HEROKU_DATABASE: String
        lateinit var HEROKU_USER: String
        lateinit var HEROKU_PORT: String
        lateinit var HEROKU_PASSWORD: String
        lateinit var HEROKU_HOST: String

        @Throws(IOException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            Property()
        }
    }

    init {
        val props = Properties()
        props.load(FileInputStream(File("src/main/resources/config/property.properties")))
        TRANSLATOR_URL = props.getProperty("Translator.URL")
        TRANSLATOR_KEY = props.getProperty("Translator.key")
        HEROKU_DATABASE = props.getProperty("Heroku.database")
        HEROKU_USER = props.getProperty("Heroku.user")
        HEROKU_PORT = props.getProperty("Heroku.port")
        HEROKU_PASSWORD = props.getProperty("Heroku.password")
        HEROKU_HOST = props.getProperty("Heroku.host")
    }
}