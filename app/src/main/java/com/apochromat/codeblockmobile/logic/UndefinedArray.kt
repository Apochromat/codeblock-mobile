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
        setBlockType("DefinedVariable")
    }
//    private fun initVar(){
//        inputName = inputLeftEdit
//    хмм, новые поля)
//    }

    fun setBlockInput(_name: String, _size: String) {
        inputName = _name
        inputSize = _size
    }

    override fun executeBlock() {
//        initVar()
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
        heap.createArray(name, size)
        for (i in 0 until size) {
            heap.setArrayValue(name, i, 0)
        }
    }

    override fun clearBlockData() {
        size = 0
        name = ""
    }
}