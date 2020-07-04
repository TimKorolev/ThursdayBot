package commands.executers

import com.ibm.cloud.sdk.core.http.HttpConfigOptions
import com.ibm.cloud.sdk.core.security.IamAuthenticator
import com.ibm.watson.language_translator.v3.LanguageTranslator
import com.ibm.watson.language_translator.v3.model.TranslateOptions
import commands.ICommandExecuter
import db.Connections.*
import db.DbHelper
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


object VocabularyExecuter : ICommandExecuter {

    private lateinit var chatId: String

    fun setChatId(_chatId: String): VocabularyExecuter {
        chatId = _chatId
        return this
    }

    override fun execute(params: List<String>): String {
        var word = params.toString().replace(",", "").replace("[", "").replace("]", "")
        val authenticator = IamAuthenticator("xvA0hvpQ9BE1hnU4IRubfQuFYuUBJL6Et22f-esf4jN-")
        val languageTranslator = LanguageTranslator("2018-05-01", authenticator)
        languageTranslator.serviceUrl =
            "https://api.eu-gb.language-translator.watson.cloud.ibm.com/instances/3e7dfd9e-ec39-42d1-a55f-98888ce2c6dc"

        val configOptions = HttpConfigOptions.Builder()
            .disableSslVerification(true)
            .build()
        languageTranslator.configureClient(configOptions)

        val translateOptions = TranslateOptions.Builder()
            .addText(word)
            .modelId("en-ru")
            .build()

        val translate = languageTranslator.translate(translateOptions)
            .execute().result.toString().split("\"translation\": \"")[1].split(
            "\"\n" +
                    "    }"
        )[0]

        writeWordAndTranslateToBd(word, translate, chatId)

        return "Запомню $word как '$translate'"
    }

    private fun writeWordAndTranslateToBd(word: String, translate: String, chatId: String) {
        if (!isUserExist(chatId)) {
            addUser(chatId)
        }
        if (!isWordExist(word, chatId)) {
            val createDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE).replace("-", ".")
            DbHelper.getConnection(HerokuDb.url)?.prepareStatement(
                "insert into words(word, translate, chat_id, rating, create_date) values ('$word','$translate','$chatId', 0, '$createDate')"
            )?.execute()
        }
    }

    private fun addUser(chatId: String) {
        DbHelper.getConnection(HerokuDb.url)?.prepareStatement(
            "insert into users(chat_id) values ('$chatId')"
        )?.execute()
    }

    private fun isUserExist(chatId: String): Boolean {
        var rows = 0
        val result =
            DbHelper.getConnection(HerokuDb.url)?.prepareStatement("select * from users where chat_id = '$chatId'")
                ?.executeQuery()

        while (result!!.next()) {
            rows++
        }

        return rows > 0
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
}