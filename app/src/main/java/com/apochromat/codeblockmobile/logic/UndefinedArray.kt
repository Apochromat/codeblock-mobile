package com.apochromat.codeblockmobile.logic

/**
 *  Блок неопределенного массива.
 *  Позволяет объявить массив.
 **/
class UndefinedArray : Block() {
    private var size: Int = 0
    private var name: String = ""
    private var inputSize: String = ""
    private var inputName: String = ""

    init {
        type = "UndefinedArray"
    }

    override fun initVar() {
        inputName = inputLeftEdit
        inputSize = inputRightEdit
    }

    override fun executeBlock() {
        super.executeBlock()
        // Инициализируем поля из ввода
        initVar()
        // Высчитаваем размер массива
        val calcSize = arithmetics(heap, inputSize)

        // Отлавливаем неправильное название
        if (!variableCheck(inputName)) {
            status = incorrectNaming(inputName)
            return
        }
        // Отлавливаем неправильный размер массива
        if (calcSize.first != ok() || calcSize.second < 1) {
            status = incorrectSize(inputSize)
            return
        }
        // Отлавливаем ситуацию, когда с таким названием уже существует переменная
        if (heap.isVariableExist(inputName)) {
            status = typeMismatchVariable(inputName)
            return
        }
        name = inputName
        size = calcSize.second
        // Создаем массив
        heap.createArray(name, size)
        // Заполняем массив нулями
        for (i in 0 until size) {
            heap.setArrayValue(name, i, 0)
        }
    }
}