package com.apochromat.codeblockmobile.logic

/**
 *  Блок консольного вывода.
 *  Позволяет вывести в консоль сообщение и значение переменной.
 **/
class ConsoleOutput : Block() {
    private var message: String = ""
    private var expression: String = ""

    init {
        setBlockType("ConsoleOutput")
    }
    private fun initVar(){
        message = inputLeftEdit
        expression = inputRightEdit
    }

    fun setBlockInput(_expression: String, _message: String = "") {
        message = if (_message == "") "" else "$_message "
        expression = _expression
    }

    override fun executeBlock() {
        initVar()
        if (accessHeap().isVariableExist(expression)) {
            println("$message ${accessHeap().getVariableValue(expression).toString()}")
            adapterConsole.addMessage("$message ${accessHeap().getVariableValue(expression).toString()}")
            return
        }
        println("$message${expression}")
        adapterConsole.addMessage("$message${expression}}")
    }
}