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

    override fun initVar() {
        inputName = inputLeftEdit
        inputValue = inputRightEdit
    }

    override fun executeBlock() {
        super.executeBlock()
        initVar()
        // Определяем, что за объект, которому мы будем присваивать
        val obj = defineInput(heap, inputName)
        name = obj.second
        // Отсеиваем ненужное
        if (obj.first !in listOf(tagArray(), tagVariable())) {
            status = obj.first
            return
        }
        // Высчитываем, что будем присваивать
        val calculated = arithmetics(heap, inputValue)
        status = calculated.first
        if (calculated.first != ok()) return
        value = calculated.second
        // Присваеваем высчитанное значение либо переменной, либо элементу массива
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