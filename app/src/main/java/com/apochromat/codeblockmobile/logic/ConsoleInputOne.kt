package com.apochromat.codeblockmobile.logic

/**
 *  Блок одиночного консольного ввода.
 *  Позволяет вывести в консоль сообщение и считать введеное значение, присвоить его переменной.
 **/
class ConsoleInputOne : Block() {
    private var message: String = ""
    private var name: String = ""
    private var value: Int = 0

    init {
        setBlockType("ConsoleInputOne")
    }

    fun setBlockInput(_name: String, _message: String = "") {
        name = _name
        message = _message
    }

    override fun executeBlock() {
        print(message)
        val inputValue: String = readln()
        val calculated = arithmetics(accessHeap(), inputValue)
        setBlockStatus(calculated.first)
        if (calculated.first == "OK") {
            value = calculated.second
            accessHeap().setVariableValue(name, value)
        }
    }
}
