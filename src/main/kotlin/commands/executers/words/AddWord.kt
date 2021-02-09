package commands.executers.words

import com.ibm.cloud.sdk.core.http.HttpConfigOptions
import com.ibm.cloud.sdk.core.security.IamAuthenticator
import com.ibm.watson.language_translator.v3.LanguageTranslator
import com.ibm.watson.language_translator.v3.model.TranslateOptions
import commands.ICommandExecutor
import commands.executers.BaseExecutor
import db.requests.WordsRequest
import property.Property
import property.Property.Companion.TRANSLATOR_URL


object AddWord : BaseExecutor(), ICommandExecutor {

    override fun execute(params: List<String>): String {
        val word = params[0]
        var translate = ""
        when (params.size) {
            1 -> {
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

                translate = languageTranslator.translate(translateOptions)
                    .execute().result.toString().split("\"translation\": \"")[1].split(
                    "\"\n" + "    }"
                )[0]
            }
            3 -> {
                translate = params[2]
            }
        }
        return WordsRequest.addWordAndTranslate(
            word, translate, chatId
        )
    }
}