package com.apochromat.codeblockmobile.logic

/**
 *  Блок консольного вывода.
 *  Позволяет вывести в консоль сообщение и значение переменной.
 **/
class ConsoleOutput : Block() {
    private var message: String = ""
    private var variable: String = ""

    init {
        setBlockType("ConsoleOutput")
    }
    private fun initVar(){
        message = inputLeftEdit
        variable = inputRightEdit
    }

    fun setBlockInput(_variable: String, _message: String = "") {
        message = _message
        variable = _variable
    }

    override fun executeBlock() {
        initVar()
        if (accessHeap().isVariableExist(variable)) {
            println("$message ${accessHeap().getVariableValue(variable).toString()}")
            adapter.addMessage("$message ${accessHeap().getVariableValue(variable).toString()}")
            return
        }
        setBlockStatus("Undefined variable $variable")

    }
}