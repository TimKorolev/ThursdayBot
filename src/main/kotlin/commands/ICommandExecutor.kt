package commands

interface ICommandExecutor {

    fun execute(params: List<String>): String
}