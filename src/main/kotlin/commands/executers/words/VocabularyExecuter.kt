package commands.executers.words

import com.ibm.cloud.sdk.core.http.HttpConfigOptions
import com.ibm.cloud.sdk.core.security.IamAuthenticator
import com.ibm.watson.language_translator.v3.LanguageTranslator
import com.ibm.watson.language_translator.v3.model.TranslateOptions
import commands.ICommandExecuter
import db.DbRequest


object VocabularyExecuter : ICommandExecuter {

    override lateinit var chatId: String

    fun setChatId(_chatId: String): VocabularyExecuter {
        chatId = _chatId
        return this
    }

    override fun execute(params: List<String>): String {
        val word = params.toString().replace(",", "").replace("[", "").replace("]", "")
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
        return DbRequest.addWordAndTranslate(word, translate,
            chatId
        )
    }

}