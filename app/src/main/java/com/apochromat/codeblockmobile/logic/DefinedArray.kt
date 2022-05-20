package com.apochromat.codeblockmobile.logic

/**
 *  Блок определенного массива.
 *  Позволяет объявить массив и установить значение элементам.
 **/
class DefinedArray : Block() {
    private var size: Int = 0
    private var name: String = ""
    private var values: String = ""
    private var inputSize: String = ""
    private var inputValues: String = ""
    private var inputName: String = ""

    init {
        type = "DefinedArray"
    }
    private fun initVar(){
        inputName = inputLeftEdit
        inputSize = inputMediumEdit
        inputValues = inputRightEdit
    }

    fun setBlockInput(_name: String, _values: String, _size: String) {
        inputName = _name
        inputValues = _values
        inputSize = _size
    }

    override fun executeBlock() {
        super.executeBlock()
        initVar()
        val calcSize = arithmetics(heap, inputSize)
        if (!variableCheck(inputName)) {
            status = incorrectNaming(inputName)
            return
        }
        if (calcSize.first != ok() || calcSize.second < 1) {
            status = incorrectSize(inputSize)
            return
        }
        if (heap.isVariableExist(inputName)) {
            status = typeMismatchVariable(inputName)
            return
        }
        name = inputName
        size = calcSize.second
        values = inputValues
        heap.createArray(name, size)
        val valuesList = stringToList(values)
        if (valuesList.size != size) {
            status = sizesMismatch()
            return
        }
        for (i in valuesList.indices) {
            val calcValue = arithmetics(heap, valuesList[i])
            if (calcValue.first != ok()) {
                status = incorrectValue(valuesList[i])
                return
            }
            heap.setArrayValue(name, i, calcValue.second)
        }
    }
}