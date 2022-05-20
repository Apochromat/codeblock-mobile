package com.apochromat.codeblockmobile.logic

/**
 *  Блок консольного вывода.
 *  Позволяет вывести в консоль сообщение и значение переменной.
 **/
class ConsoleOutput : Block() {
    private var message: String = ""
    private var expression: String = ""

    init {
        type = "ConsoleOutput"
    }
    private fun initVar(){
        message = if (inputLeftEdit == "") "" else "$inputLeftEdit "
        expression = inputRightEdit
    }

    fun setBlockInput(_expression: String, _message: String = "") {
        message = if (_message == "") "" else "$_message "
        expression = _expression
    }

    override fun executeBlock() {
        super.executeBlock()
        initVar()
        if (expression == "") {
            adapterConsole.addMessage(message)
            return
        }
        if (heap.isArrayExist(expression)) {
            adapterConsole.addMessage("$message[${heap.getArray(expression)?.joinToString()}]")
            return
        }
        val calculated = arithmetics(heap, expression)
        status = calculated.first
        if (calculated.first != ok()) return
        adapterConsole.addMessage("$message${calculated.second}")
    }
}