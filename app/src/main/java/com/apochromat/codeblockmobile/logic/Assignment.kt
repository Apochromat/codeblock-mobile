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
        val obj = defineInput(heap, inputName)
        name = obj.second
        if (obj.first !in listOf("Array", "Variable")) {
            setBlockStatus(obj.first)
            return
        }
        val calculated = arithmetics(heap, inputValue)
        setBlockStatus(calculated.first)
        if (calculated.first != "OK") return
        value = calculated.second
        when (obj.first) {
            "Array" -> {
                heap.setArrayValue(name, obj.third, value)
            }
            "Variable" -> {
                heap.setVariableValue(name, value)
            }
        }
    }
}