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
        initVar()
        var flag: Boolean = true
        for (el in inputNames) {
            if (!variableCheck(el)) {
                setBlockStatus("Incorrect variable naming $el")
                flag = false
            }
        }
        if (flag) accessHeap().createDefaultVariables(inputNames)
    }
}