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

    override fun initVar() {
        message = if (inputLeftEdit == "") "" else "$inputLeftEdit "
        expression = inputRightEdit
    }

    override fun executeBlock() {
        super.executeBlock()
        // Инициализируем поля из ввода
        initVar()

        // Выводим сообщение
        if (expression == "") {
            adapterConsole.addMessage(message)
            return
        }
        // Выводим массив целиком
        if (heap.isArrayExist(expression)) {
            adapterConsole.addMessage("$message[${heap.getArray(expression)?.joinToString()}]")
            return
        }
        // Выводим значение переменной или элемента массива
        val calculated = arithmetics(heap, expression)
        status = calculated.first
        if (calculated.first != ok()) return
        adapterConsole.addMessage("$message${calculated.second}")
    }
}