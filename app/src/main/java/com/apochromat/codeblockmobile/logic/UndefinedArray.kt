package com.apochromat.codeblockmobile.logic

/**
 *  Блок неопределенного массива.
 *  Позволяет объявить массив.
 **/
class UndefinedArray : Block() {
    private var size: Int = 0
    private var name: String = ""
    private var inputSize: String = ""
    private var inputName: String = ""

    init {
        setBlockType("UndefinedArray")
    }
    private fun initVar(){
        inputName = inputLeftEdit
        inputSize = inputRightEdit
    }

    fun setBlockInput(_name: String, _size: String) {
        inputName = _name
        inputSize = _size
    }

    override fun executeBlock() {
        super.executeBlock()
        initVar()
        val calcSize = arithmetics(heap, inputSize)
        if (!variableCheck(inputName)) {
            setBlockStatus(incorrectNaming(inputName))
            return
        }
        if (calcSize.first != ok() || calcSize.second < 1) {
            setBlockStatus(incorrectSize(inputSize))
            return
        }
        if (heap.isVariableExist(inputName)) {
            setBlockStatus(typeMismatchVariable(inputName))
            return
        }
        name = inputName
        size = calcSize.second
        heap.createArray(name, size)
        for (i in 0 until size) {
            heap.setArrayValue(name, i, 0)
        }
    }
}