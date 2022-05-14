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
        setBlockType("DefinedArray")
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
        initVar()
        val calcSize = arithmetics(heap, inputSize)
        if (!variableCheck(inputName)) {
            setBlockStatus("Incorrect naming $inputName")
            return
        }
        if (calcSize.first != "OK" || calcSize.second < 1) {
            setBlockStatus("Incorrect size $inputSize")
            return
        }
        if (heap.isVariableExist(inputName)) {
            setBlockStatus("Type mismatch, $inputName is an existing variable")
            return
        }
        name = inputName
        size = calcSize.second
        values = inputValues
        heap.createArray(name, size)
        val valuesList = stringToList(values)
        if (valuesList.size != size) {
            setBlockStatus("Sizes mismatch")
            return
        }
        for (i in valuesList.indices) {
            val calcValue = arithmetics(heap, valuesList[i])
            if (calcValue.first != "OK") {
                setBlockStatus("Incorrect value '$valuesList[i]'")
                return
            }
            heap.setArrayValue(name, i, calcValue.second)
        }
    }
}