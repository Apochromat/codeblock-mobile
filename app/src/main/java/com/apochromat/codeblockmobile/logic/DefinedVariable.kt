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
        setBlockType("DefinedVariable")
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

        initVar()
        if (variableCheck(inputName)) {
            val calculated = arithmetics(accessHeap(), inputValue)
            setBlockStatus(calculated.first)
            name = inputName
            if (calculated.first == "OK") {
                value = calculated.second
                accessHeap().setVariableValue(name, value)
            }
        } else {
            setBlockStatus("Incorrect variable naming $inputName")
        }
    }

    override fun clearBlockData() {
        value = 0
        name = ""
    }
}