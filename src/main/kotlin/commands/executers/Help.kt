package commands.executers

import commands.ICommandExecutor


object Help : BaseExecutor(), ICommandExecutor {

    override fun execute(params: List<String>): String {

        return """Commands:
1) <word> (as (<custom translate>))
    - use \_ for compound word
    - use (<custom translate>),(<custom translate>) for multiple meanings
2) help
3) delete <word>
"""
    }

}