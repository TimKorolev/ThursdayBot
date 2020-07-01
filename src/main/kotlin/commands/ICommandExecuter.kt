package commands

interface ICommandExecuter {

    fun execute(params: List<String>) : Any
}