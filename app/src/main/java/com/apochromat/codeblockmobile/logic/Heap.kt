package com.apochromat.codeblockmobile.logic

class Heap {
    private var heap: MutableMap<String, Int> = mutableMapOf()
    private var heapArray: MutableMap<String, Array<Int>> = mutableMapOf()

    /**
     * Создать новые переменные со значением 0 из списка names.
     **/
    fun createDefaultVariables(names: List<String>) {
        for (name: String in names) {
            heap[name] = 0
        }
    }

    /**
     * Установить переменной name значение value.
     **/
    fun setVariableValue(name: String, value: Int) {
        heap[name] = value
    }

    /**
     * Получить значение переменной names.
     **/
    fun getVariableValue(name: String): Int? {
        return heap[name]
    }

    /**
     * Проверить, существует ли переменная name.
     **/
    fun isVariableExist(name: String): Boolean {
        return name in heap.keys
    }

    /**
     * Получить список с названием всех переменных.
     **/
    fun getVariablesList(): MutableSet<String> {
        return heap.keys
    }

    /**
     *  Удалить переменную name.
     **/
    fun deleteVariable(name: String) {
        heap.remove(name)
    }

    /**
     *  Удалить все переменные.
     **/
    fun clearVariables() {
        heap.clear()
    }

    /**
     * Создать новый пустой массив.
     **/
    fun createArray(arrayName: String, arraySize: Int) {
        heapArray[arrayName] = Array(arraySize) { 0 }
    }

    /**
     * Удалить массив.
     **/
    fun deleteArray(arrayName: String) {
        heapArray.remove(arrayName)
    }

    /**
     * Установить значение элемента в массиве.
     **/
    fun setArrayValue(arrayName: String, index: Int, value: Int) {
        heapArray[arrayName]?.set(index, value)
    }

    /**
     * Получить значение элемента в массиве.
     **/
    fun getArrayValue(arrayName: String, index: Int): Int? {
        return heapArray[arrayName]?.get(index)
    }

    /**
     * Получить значение массив.
     **/
    fun getArray(arrayName: String): Array<Int>? {
        return heapArray[arrayName]
    }

    /**
     * Существует ли массив.
     **/
    fun isArrayExist(arrayName: String): Boolean {
        return arrayName in heapArray.keys
    }

    /**
     * Получить список с названием всех массивов.
     **/
    fun getArraysList(): MutableSet<String> {
        return heapArray.keys
    }

    /**
     * Получить размер массива.
     **/
    fun getArraySize(arrayName: String): Int? {
        return heapArray[arrayName]?.size
    }

    /**
     *  Удалить все массивы.
     **/
    fun clearArrays() {
        heapArray.clear()
    }
}