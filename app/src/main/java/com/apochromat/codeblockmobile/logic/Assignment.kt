package com.apochromat.codeblockmobile.logic

/**
 *  Блок присвоения.
 *  Позволяет установить существующей переменной значение.
 **/
class Assignment : Block() {
    private var value: Int = 0
    private var name: String = ""
    private var inputValue: String = ""
    private var inputName: String = ""

    init {
        setBlockType("Assignment")
    }

    fun setBlockInput(_name: String, _value: String) {
        inputName = _name
        inputValue = _value
    }

    override fun executeBlock() {
        if (variableCheck(inputName)) {
            if (accessHeap().isVariableExist(inputName)) {
                val calculated = arithmetics(accessHeap(), inputValue)
                setBlockStatus(calculated.first)
                name = inputName
                if (calculated.first == "OK") {
                    value = calculated.second
                    accessHeap().setVariableValue(name, value)
                }
            } else {
                setBlockStatus("Undefined variable $inputName")
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