package com.apochromat.codeblockmobile.logic

/**
 *  Блок неопределенной переменной.
 *  Позволяет объявить переменные со значением по умолчанию (0).
 **/
class UndefinedVariable : Block() {
    private var inputNames: List<String> = listOf()

    init {
        setBlockType("UndefinedVariable")
    }

    private fun initVar(){
        inputNames = stringToList(inputLeftEdit)
    }

    fun setBlockInput(_names: String) {
        inputNames = stringToList(_names)
    }

    override fun executeBlock() {
        super.executeBlock()
        initVar()
        var flag = true
        for (el in inputNames) {
            if (heap.isArrayExist(el)) {
                setBlockStatus(typeMismatchArray(el))
                flag = false
            }
            if (!variableCheck(el)) {
                setBlockStatus(incorrectNaming(el))
                flag = false
            }
        }
        if (flag) accessHeap().createDefaultVariables(inputNames)
    }
}