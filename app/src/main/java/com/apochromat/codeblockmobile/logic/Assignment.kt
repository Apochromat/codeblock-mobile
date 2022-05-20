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
        type = "Assignment"
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
        val obj = defineInput(heap, inputName)
        name = obj.second
        if (obj.first !in listOf(tagArray(), tagVariable())) {
            status = obj.first
            return
        }
        val calculated = arithmetics(heap, inputValue)
        status = calculated.first
        if (calculated.first != ok()) return
        value = calculated.second
        when (obj.first) {
            tagArray() -> {
                heap.setArrayValue(name, obj.third, value)
            }
            tagVariable() -> {
                heap.setVariableValue(name, value)
            }
        }
    }
}