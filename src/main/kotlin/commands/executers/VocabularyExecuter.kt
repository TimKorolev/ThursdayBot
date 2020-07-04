package commands.executers

import com.ibm.cloud.sdk.core.http.HttpConfigOptions
import com.ibm.cloud.sdk.core.security.IamAuthenticator
import com.ibm.watson.language_translator.v3.LanguageTranslator
import com.ibm.watson.language_translator.v3.model.TranslateOptions
import com.jayway.jsonpath.Criteria
import com.jayway.jsonpath.Filter
import com.jayway.jsonpath.JsonPath
import commands.ICommandExecuter


object VocabularyExecuter : ICommandExecuter {

    override fun execute(params: List<String>): String {
        var word = params[0]
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
            .execute().result.toString().split("\"translation\": \"")[1].split("\"\n" +
                "    }")[0]

        return "Запомню $word как '$translate'"
    }
}