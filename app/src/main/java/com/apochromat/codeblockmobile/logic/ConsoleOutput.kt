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
//    private fun initVar(){
//        message = inputLeftEdit
//        expression = inputRightEdit
//    }

    fun setBlockInput(_expression: String, _message: String = "") {
        message = _message
        expression = _expression
    }

    override fun executeBlock() {
//        initVar()
        val calculated = arithmetics(heap, expression)
        if (calculated.first != "OK") {
            setBlockStatus(calculated.first)
            return
        }
        println("$message ${calculated.second}")
//            adapter.addMessage("$message ${calculated.second}}")
    }
}