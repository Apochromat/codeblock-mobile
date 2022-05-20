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

    override fun initVar() {
        inputName = inputLeftEdit
        inputSize = inputMediumEdit
        inputValues = inputRightEdit
    }

    override fun executeBlock() {
        super.executeBlock()
        // Инициализируем поля из ввода
        initVar()
        // Высчитаваем размер массива
        val calcSize = arithmetics(heap, inputSize)

        // Отлавливаем неправильное название
        if (!variableCheck(inputName)) {
            status = incorrectNaming(inputName)
            return
        }
        // Отлавливаем неправильный размер массива
        if (calcSize.first != ok() || calcSize.second < 1) {
            status = incorrectSize(inputSize)
            return
        }
        // Отлавливаем ситуацию, когда с таким названием уже существует переменная
        if (heap.isVariableExist(inputName)) {
            status = typeMismatchVariable(inputName)
            return
        }
        name = inputName
        size = calcSize.second
        values = inputValues
        // Создаем массив
        heap.createArray(name, size)
        // Делаем список из входных данных "1, 2, 3+1, 4 - a" => ["1", "2", "3+1", "4-a"]
        val valuesList = stringToList(values)
        // Отлавливаем несоответствие размеров массива и количества введеных чисел
        if (valuesList.size != size) {
            status = sizesMismatch()
            return
        }
        // Для каждого элемента ввода высчитаваем его и присваеваем массиву
        // Если хоть один неверный, прекращаем работу
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