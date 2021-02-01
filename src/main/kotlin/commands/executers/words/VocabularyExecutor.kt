package commands.executers.words

import com.ibm.cloud.sdk.core.http.HttpConfigOptions
import com.ibm.cloud.sdk.core.security.IamAuthenticator
import com.ibm.watson.language_translator.v3.LanguageTranslator
import com.ibm.watson.language_translator.v3.model.TranslateOptions
import commands.ICommandExecutor
import commands.executers.BaseExecutor
import db.requests.DbRequest
import property.Property
import property.Property.Companion.TRANSLATOR_URL


object VocabularyExecutor : BaseExecutor(), ICommandExecutor {

    override fun execute(params: List<String>): String {
        val word = params.toString().replace(",", "").replace("[", "").replace("]", "")
        val authenticator = IamAuthenticator(Property.TRANSLATOR_KEY)
        val languageTranslator = LanguageTranslator("2018-05-01", authenticator)
        languageTranslator.serviceUrl = TRANSLATOR_URL

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
        return DbRequest.addWordAndTranslate(
            word, translate,
            chatId
        )
    }

}