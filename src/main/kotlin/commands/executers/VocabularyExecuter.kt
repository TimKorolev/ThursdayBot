package commands.executers

import api.ApiHandler
import api.requests.Request.GET_TRANSLATE
import api.requests.RequestBuilder
import commands.ICommandExecuter

object VocabularyExecuter : ICommandExecuter {

    override fun execute(params: List<String>): String {
        var word = params[0]
        ApiHandler().execute(RequestBuilder.getHttpRequest(GET_TRANSLATE))
        return "Запомню $word"
    }
}