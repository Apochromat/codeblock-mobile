package com.apochromat.codeblockmobile.logic

/**
 *  Блок определенной переменной.
 *  Позволяет объявить переменную и установить ей значение.
 **/
class DefinedVariable : Block() {
    private var value: Int = 0
    private var name: String = ""
    private var inputValue: String = ""
    private var inputName: String = ""

    init {
        type = "DefinedVariable"
    }

    override fun initVar() {
        inputName = inputLeftEdit
        inputValue = inputRightEdit
    }

    override fun executeBlock() {
        super.executeBlock()
        // Инициализируем поля из ввода
        initVar()

        // Отлавливаем неправильное название
        if (!variableCheck(inputName)) {
            status = incorrectNaming(inputName)
            return
        }
        // Отлавливаем ситуацию, когда с таким названием уже существует массив
        if (heap.isArrayExist(inputName)) {
            status = typeMismatchArray(inputName)
            return
        }

        // Высчитываем значение для переменной
        val calculated = arithmetics(heap, inputValue)
        status = calculated.first
        name = inputName
        // Присваиваем переменной значение
        if (calculated.first == ok()) {
            value = calculated.second
            heap.setVariableValue(name, value)
        }
    }
}