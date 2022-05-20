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
    private fun initVar(){
        inputName = inputLeftEdit
        inputValue = inputRightEdit
    }

    fun setBlockInput(_name: String, _value: String) {
        inputName = _name
        inputValue = _value
    }

    override fun executeBlock() {
        super.executeBlock()
        initVar()
        if (heap.isArrayExist(inputName)) {
            status = typeMismatchArray(inputName)
            return
        }
        if (!variableCheck(inputName)) {
            status = incorrectNaming(inputName)
            return
        }
        val calculated = arithmetics(accessHeap(), inputValue)
        status = calculated.first
        name = inputName
        if (calculated.first == ok()) {
            value = calculated.second
            accessHeap().setVariableValue(name, value)
        }
    }
}