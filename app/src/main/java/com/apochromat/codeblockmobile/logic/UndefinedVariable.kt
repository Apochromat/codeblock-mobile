package com.apochromat.codeblockmobile.logic

/**
 *  Блок неопределенной переменной.
 *  Позволяет объявить переменные со значением по умолчанию (0).
 **/
class UndefinedVariable : Block() {
    private var inputNames: List<String> = listOf()

    init {
        type = "UndefinedVariable"
    }

    override fun initVar() {
        // Делаем список из входных данных "a, B, Pussycat_17" => ["a", "B", "Pussycat_17"]
        inputNames = stringToList(inputLeftEdit)
    }

    override fun executeBlock() {
        super.executeBlock()
        // Инициализируем поля из ввода
        initVar()
        // Проверяем все переменные на правильность
        var flag = true
        for (el in inputNames) {
            // Отлавливаем неправильное название
            if (!variableCheck(el)) {
                status = incorrectNaming(el)
                flag = false
            }
            // Отлавливаем ситуацию, когда с таким названием уже существует массив
            if (heap.isArrayExist(el)) {
                status = typeMismatchArray(el)
                flag = false
            }
        }
        // Создаем переменные, наполненные нулями
        if (flag) heap.createDefaultVariables(inputNames)
    }
}