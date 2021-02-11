package commands.executers

import commands.ICommandExecutor
import commands.executers.BaseExecutor
import constant.UserRatingDependency
import db.requests.UserRequests
import db.requests.WordsRequest


object Statistics : BaseExecutor(), ICommandExecutor {

    override fun execute(params: List<String>): String {

        val rating = UserRequests.getUserRating(chatId)
        val pointsToTheNextLevel: Int = UserRatingDependency.values()
            .find{ level -> level.rating > UserRequests.getUserRating(chatId) }!!.rating - rating
        val dictionarySize = WordsRequest.getNumberOfWord(chatId)

        return """ Statistics:
            Rating: $rating,
            Points to the next level: $pointsToTheNextLevel,
            Dictionary size: $dictionarySize
        """.trimIndent()
    }
}