package com.apochromat.codeblockmobile

import java.util.*

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
    fun setVariableValue(name: String, value: Int) { heap[name] = value }

    /**
     * Получить значение переменной names.
     **/
    fun getVariableValue(name: String): Int? { return heap[name] }

    /**
     * Проверить, существует ли переменная name.
     **/
    fun isVariableExist(name: String): Boolean { return name in heap.keys }

    /**
     * Получить список с названием всех переменных.
     **/
    fun getVariablesList(): MutableSet<String> { return heap.keys }

    /**
     *  Удалить переменную name.
     **/
    fun deleteVariable(name: String) { heap.remove(name) }

    /**
     *  Удалить все переменные.
     **/
    fun clearVariables() { heap.clear() }

    /**
     * Создать новый пустой массив.
     **/
    fun createArray(arrayName: String, arraySize: Int) {
        heapArray[arrayName] = Array(arraySize, {0})
    }

    /**
     * Удалить массив.
     **/
    fun removeArray(arrayName: String) {
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
     * Существует ли массив.
     **/
    fun isArrayExist(arrayName: String): Boolean {
        return arrayName in heapArray.keys
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
    fun clearArrays() { heapArray.clear() }
}

/**
 *  Основной блок.
 **/
open class Block {
    companion object {
        var callStack: Stack<Block> = Stack()
        //  Хранилище переменных
        var heap: Heap = Heap()
        //  Счетчик блоков
        var counter: Int = 0
        //  Словарь со всеми блоками
        var allBlocks: MutableMap<Int, Block> = mutableMapOf()
        //  Список устойчивых связей между блоками
        var strongConnections: MutableList<Pair<Block, Block>> = mutableListOf()
    }

    //  Ссылки на следующий и предыдущий блоки
    private var nextBlock: Block? = null
    private var prevBlock: Block? = null
    //  Тип, статус и идентификатор блока
    private var type: String = "Undefined"
    private var status: String = "OK"
    private var id: Int = counter++

    init {
        allBlocks[this.getBlockId()] = this
    }

    //  Операции с типом, статусом и идентификатором блока
    fun setBlockType(input: String) { type = input }
    fun getBlockType(): String { return type }
    fun setBlockStatus(input: String) { status = input }
    fun getBlockStatus(): String { return status }
    fun getBlockId(): Int { return id }
    fun getBlockById(_id : Int): Block? { return allBlocks[_id] }

    //  Получить словарь всех блоков
    fun getAllBlocks(): MutableMap<Int, Block> { return allBlocks }
    //  Получить доступ к хранилищу переменных
    fun accessHeap(): Heap { return heap }

    //  Операции с ссылками на предыдущий и следующий блоки
    fun setNextBlock(block: Block?) { nextBlock = block }
    fun getNextBlock(): Block? { return nextBlock }
    fun setPrevBlock(block: Block?) { prevBlock = block }
    fun getPrevBlock(): Block? { return prevBlock }

    //  Операции со списком устойчивых связей между блоками
    fun addStrongConnection(pair: Pair<Block, Block>) { strongConnections.add(pair) }
    fun removeStrongConnection(pair: Pair<Block, Block>) { strongConnections.remove(pair) }
    fun getAllStrongConnections(): MutableList<Pair<Block, Block>> { return strongConnections }

    open fun executeBlock() {}
    open fun clearBlockData() {}

    open fun run() {
        executeBlock()
        when {
            getNextBlock() == null -> {
                println("Program finished with status: ${getBlockStatus()}")
            }
            getBlockStatus() == "OK" -> {
                callStack.push(getNextBlock())
            }
            else -> {
                println("Program finished with status: ${getBlockStatus()}")
            }
        }
    }
}

/**
 *  Точка входа в программу.
 **/
class EntryPoint: Block() {
    init {
        setBlockType("EntryPoint")
    }
    override fun executeBlock() {
        accessHeap().clearVariables()
        for (bl in getAllBlocks()) {
            bl.value.clearBlockData()
        }
        for (pair in getAllStrongConnections()) {
            connectBlocks(pair.first, pair.second, false)
        }
    }
    override fun run() {
        super.run()
        while (!callStack.empty()) {
            callStack.pop().run()
        }
    }
}

/**
 *  Блок определенной переменной.
 *  Позволяет объявить переменную и установить ей значение.
 **/
class DefinedVariable: Block() {
    private var value: Int = 0
    private var name: String = ""
    private var inputValue: String = ""
    private var inputName: String = ""
    init {
        setBlockType("DefinedVariable")
    }
    fun setBlockInput( _name: String, _value: String) {
        inputName = _name
        inputValue = _value
    }
    override fun executeBlock() {
        if (variableCheck(inputName)) {
            val calculated = arithmetics(accessHeap(), inputValue)
            setBlockStatus(calculated.first)
            name = inputName
            if (calculated.first == "OK") {
                value = calculated.second
                accessHeap().setVariableValue(name, value)
            }
        }
        else {
            setBlockStatus("Incorrect variable naming $inputName")
        }
    }
    override fun clearBlockData() {
        value = 0
        name = ""
    }
}

/**
 *  Блок неопределенной переменной.
 *  Позволяет объявить переменные со значением по умолчанию (0).
 **/
class UndefinedVariable: Block() {
    private var inputNames: List<String> = listOf()
    init {
        setBlockType("UndefinedVariable")
    }
    fun setBlockInput(_names: String) {
        inputNames = stringToList(_names)
    }
    override fun executeBlock() {
        var flag: Boolean = true
        for (el in inputNames) {
            if (!variableCheck(el)) {
                setBlockStatus("Incorrect variable naming $el")
                flag = false
            }
        }
        if (flag) accessHeap().createDefaultVariables(inputNames)
    }
}

/**
 *  Блок присвоения.
 *  Позволяет установить существующей переменной значение.
 **/
class Assignment: Block() {
    private var value: Int = 0
    private var name: String = ""
    private var inputValue: String = ""
    private var inputName: String = ""
    init {
        setBlockType("Assignment")
    }
    fun setBlockInput( _name: String, _value: String) {
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
        }
        else {
            setBlockStatus("Incorrect variable naming $inputName")
        }
    }
    override fun clearBlockData() {
        value = 0
        name = ""
    }
}

/**
 *  Служебный блок.
 **/
class BeginEnd: Block() {
    init {
        setBlockType("BeginEnd")
    }
}

/**
 *  Блок условия If.
 *  Позволяет выполнять определенный набор команд при выполнении условия.
 **/
class ConditionIf: Block() {
    private var expressionLeft: String = ""
    private var expressionRight: String = ""
    private var expressionComparator: String = ">="
    var ifBegin: BeginEnd = BeginEnd()
    var ifEnd: BeginEnd = BeginEnd()
    init {
        setBlockType("ConditionIf")
    }
    fun setBlockInput(_expressionLeft: String, _expressionRight: String, _expressionComparator: String = ">=") {
        expressionLeft = _expressionLeft
        expressionRight = _expressionRight
        expressionComparator = _expressionComparator
    }
    override fun executeBlock() {
        getNextBlock()?.let { connectBlocks(ifEnd, it, strong = true, clear = false) }

        if (expressionComparator !in listOf(">", ">=", "<", "<=", "==", "!=")) {
            setBlockStatus("Invalid comparator")
        }
        else {
            val calculateLeft = arithmetics(accessHeap(), expressionLeft)
            val calculateRight = arithmetics(accessHeap(), expressionRight)
            if ((calculateLeft.first == "OK") && (calculateRight.first == "OK")) {
                if (expressionComparator(calculateLeft.second, calculateRight.second, expressionComparator)) {
                    connectBlocks(this, ifBegin, false)
                }
                else {
                    connectBlocks(this, ifEnd, false)
                }
            } else {
                setBlockStatus(if (calculateLeft.first == "OK") calculateRight.first else calculateLeft.first)
            }
        }
    }
}

/**
 *  Блок условия IfElse.
 *  Позволяет выполнять определенные наборы команд при выполнении и не выполнении условия.
 **/
class ConditionIfElse: Block() {
    private var expressionLeft: String = ""
    private var expressionRight: String = ""
    private var expressionComparator: String = ">="
    var ifBegin: BeginEnd = BeginEnd()
    var ifEnd: BeginEnd = BeginEnd()
    var elseBegin: BeginEnd = BeginEnd()
    var elseEnd: BeginEnd = BeginEnd()
    init {
        setBlockType("ConditionIfElse")
    }
    fun setBlockInput(_expressionLeft: String, _expressionRight: String, _expressionComparator: String = ">=") {
        expressionLeft = _expressionLeft
        expressionRight = _expressionRight
        expressionComparator = _expressionComparator
    }
    override fun executeBlock() {
        getNextBlock()?.let { connectBlocks(ifEnd, it, strong = true, clear = false) }
        getNextBlock()?.let { connectBlocks(elseEnd, it, strong = true, clear = false) }

        if (expressionComparator !in listOf(">", ">=", "<", "<=", "==", "!=")) {
            setBlockStatus("Invalid comparator")
        }
        else {
            val calculateLeft = arithmetics(accessHeap(), expressionLeft)
            val calculateRight = arithmetics(accessHeap(), expressionRight)
            if ((calculateLeft.first == "OK") && (calculateRight.first == "OK")) {
                if (expressionComparator(calculateLeft.second, calculateRight.second, expressionComparator)) {
                    connectBlocks(this, ifBegin, false)
                }
                else {
                    connectBlocks(this, elseBegin, false)
                }
            } else {
                setBlockStatus(if (calculateLeft.first == "OK") calculateRight.first else calculateLeft.first)
            }
        }
    }
}

/**
 *  Блок цикла While.
 *  Позволяет выполнять определенный набор команд при выполнении условия.
 **/
class CycleWhile: Block() {
    private var expressionLeft: String = ""
    private var expressionRight: String = ""
    private var expressionComparator: String = ">="
    var cycleBegin: BeginEnd = BeginEnd()
    var cycleEnd: BeginEnd = BeginEnd()
    private var cycleExit: BeginEnd = BeginEnd()
    init {
        setBlockType("CycleWhile")
    }
    fun setBlockInput(_expressionLeft: String, _expressionRight: String, _expressionComparator: String = ">=") {
        expressionLeft = _expressionLeft
        expressionRight = _expressionRight
        expressionComparator = _expressionComparator
    }
    override fun executeBlock() {
        connectBlocks(cycleEnd, this, strong = false, clear = false)
        getNextBlock()?.let { if (getNextBlock() != cycleBegin )
            connectBlocks(cycleExit, it, strong = true, clear = false) }

        if (expressionComparator !in listOf(">", ">=", "<", "<=", "==", "!=")) {
            setBlockStatus("Invalid comparator")
        }
        else {
            val calculateLeft = arithmetics(accessHeap(), expressionLeft)
            val calculateRight = arithmetics(accessHeap(), expressionRight)
            if ((calculateLeft.first == "OK") && (calculateRight.first == "OK")) {
                if (expressionComparator(calculateLeft.second, calculateRight.second, expressionComparator)) {
                    connectBlocks(this, cycleBegin, strong = false, clear = false)
                }
                else {
                    connectBlocks(this, cycleExit, strong = false, clear = false)
                }
            } else {
                setBlockStatus(if (calculateLeft.first == "OK") calculateRight.first else calculateLeft.first)
            }
        }
    }
}

/**
 *  Блок консольного вывода.
 *  Позволяет вывести в консоль сообщение и значение переменной.
 **/
class ConsoleOutput: Block() {
    private var message: String = ""
    private var variable: String = ""
    init {
        setBlockType("ConsoleOutput")
    }
    fun setBlockInput( _variable: String, _message: String = "") {
        message = _message
        variable = _variable
    }
    override fun executeBlock() {
        if (accessHeap().isVariableExist(variable)) {
            println("$message ${accessHeap().getVariableValue(variable).toString()}")
        }
        else {
            setBlockStatus("Undefined variable $variable")
        }
    }
}

/**
 *  Блок одиночного консольного ввода.
 *  Позволяет вывести в консоль сообщение и считать введеное значение, присвоить его переменной.
 **/
class ConsoleInputOne: Block() {
    private var message: String = ""
    private var name: String = ""
    private var value: Int = 0
    init {
        setBlockType("ConsoleOutput")
    }
    fun setBlockInput( _name: String, _message: String = "") {
        name = _name
        message = _message
    }
    override fun executeBlock() {
        print(message)
        val inputValue: String = readln()
        val calculated = arithmetics(accessHeap(), inputValue)
        setBlockStatus(calculated.first)
        if (calculated.first == "OK") {
            value = calculated.second
            accessHeap().setVariableValue(name, value)
        }
    }
}

fun expressionComparator(numberLeft: Int, numberRight: Int, comparator: String): Boolean {
    when (comparator) {
        ">" -> return (numberLeft > numberRight)
        ">=" -> return (numberLeft >= numberRight)
        "<" -> return (numberLeft < numberRight)
        "<=" -> return (numberLeft <= numberRight)
        "==" -> return (numberLeft == numberRight)
        "!=" -> return (numberLeft != numberRight)
    }
    return false
}

/**
 *  Функция связывания блоков.
 *  Позволяет связать блоки blockFrom и blockTo.
 *  Параметры: strong - создать ли устойчивую связь, clear - очищать ли предыдущие связи.
 **/
fun connectBlocks(blockFrom: Block, blockTo: Block, strong: Boolean = true, clear: Boolean = true) {
    if (strong) Block().addStrongConnection(Pair(blockFrom, blockTo))
    if (clear) {
        blockFrom.getNextBlock()?.setPrevBlock(null)
        blockTo.getPrevBlock()?.setNextBlock(null)
    }
    blockFrom.setNextBlock(blockTo)
    blockTo.setPrevBlock(blockFrom)
}

/**
 *  Функция развязывания блоков.
 *  Позволяет развязать блоки blockFrom и blockTo.
 **/
fun disconnectBlocks(blockFrom: Block, blockTo: Block) {
    blockFrom.setNextBlock(null)
    blockTo.setPrevBlock(null)
    Block().removeStrongConnection(Pair(blockFrom, blockTo))
}

fun arithmetics(heap: Heap, expression: String): Pair<String, Int> {
    val exp = expression.replace("\\s".toRegex(), "")
    if(exp.isEmpty()) {
        return Pair("Empty input", 0)
    }
    val (prepared, expStatus) = preparingExpression(heap, exp)
    val (correctLine, lineStatus) = lineCheck(exp)
    if (expStatus == 0) {
        return Pair(prepared, 0)
    }
    if (lineStatus == 0) {
        return Pair(correctLine, 0)
    }

    return rpnToAnswer(expressionToRPN(prepared))
}

fun getPriority(token: Char): Int {
    return when (token) {
        '*', '/','%' -> 3
        '+', '-' -> 2
        '(' -> 1
        ')' -> -1
        else -> 0
    }
}

fun expressionToRPN(expression: String): String {
    var current = ""
    val stack: Stack<Char> = Stack<Char>()
    var priority: Int
    for (i in expression.indices) {
        priority = getPriority(expression[i])
        when (priority) {
            0 -> current += expression[i]
            1 -> stack.push(expression[i])
            2, 3 -> {
                current += " "
                while (!stack.empty()) {
                    if ((getPriority(stack.peek()) >= priority)) current += stack.pop()
                    else break
                }
                stack.push(expression[i])
            }
            -1 -> {
                current += " "
                while (getPriority(stack.peek()) != 1) current += stack.pop()
                stack.pop()
            }
            else -> {
                return "Error"
            }
        }
    }
    while (!stack.empty()) current += stack.pop()
    return current
}

fun rpnToAnswer(rpn: String): Pair<String,Int> {
    var operand = String()
    val stack: Stack<Int> = Stack<Int>()
    var i = 0
    while (i < rpn.length) {
        if (rpn[i] == ' ') {
            i++
            continue
        }
        if (getPriority(rpn[i]) == 0) {
            while (rpn[i] !=' ' && (getPriority(rpn[i]) == 0)) {
                operand += rpn[i++]
                if (i == rpn.length) break
            }
            try {
                stack.push(operand.toInt())
            } catch (e:NumberFormatException){
                return Pair("Unexpected symbol $operand", 0)
            }
            operand = String()
        }
        if (i == rpn.length) break
        if (getPriority(rpn[i]) > 1) {
            try{
            val a: Int = stack.pop()
            val b: Int = stack.pop()
            when (rpn[i]) {
                '+' -> stack.push(b + a)
                '-' -> stack.push(b - a)
                '*' -> stack.push(b * a)
                '/' -> {
                        try {
                            stack.push(b / a)
                        } catch (e:Exception){
                            return Pair ("Division by zero", 0)
                        }
                    }
                '%' -> stack.push(b % a)
                else -> {
                    return Pair("Unexpected symbol ${rpn[i]}", 0)
                }
                }
            } catch (e:EmptyStackException) {
                return Pair("Incorrect expression",0)
            }
        }
        i++
    }
    return Pair("OK", stack.pop())
}

fun lineCheck (string:String): Pair<String,Int>{
    val str = string.replace("[A-Za-z-+*/0-9()%]".toRegex(),"")
    if(str.isNotEmpty()){
        return Pair("Unexpected Symbol", 0)
    }
    val reg="[A-Za-z]+[0-9]|[0-9][A-Za-z]".toRegex()
    val match=reg.find(string)
    if (match != null) {
        return Pair("Incorrect expression", 0)
    }
    return Pair("OK", 1)
}

fun preparingExpression (heap: Heap, expression:String):Pair<String,Int> {
    var exp = expression
    var preparedExpression = String()
    var i = 0
    while (i < expression.length) {
        if(expression[i].code in 65..127){
            var operand = String()
            while (expression[i] != ' ' && (expression[i].code in 65..127)){
                operand += expression[i++]
                if (i == expression.length) break
            }
            if(!heap.isVariableExist(operand)){
                return Pair("Undefined variable $operand", 0)
            }
            val fromVarToNum = heap.getVariableValue(operand)

            exp = expression.replace(operand, fromVarToNum.toString())
        }
        i++
    }

    for(j in exp.indices) {
        if(exp[j] == '-') {
            if (j == 0) {
                preparedExpression += "0"
            }
            else if(exp[j-1] == '(') {
                preparedExpression += "0"
            }
        }
        preparedExpression += exp[j]
    }

    return Pair(preparedExpression, 1)
}

fun variableCheck(variable: String): Boolean{
    val str = variable.replace("[A-Za-z]".toRegex(),"")
    if(str.isNotEmpty()){
       return false
    }
    return true
}

fun stringToList(string: String): List<String> {
    return string.replace("[,;]".toRegex()," ").split("[\\s+]".toRegex()).filter { it.length > 0 }
}