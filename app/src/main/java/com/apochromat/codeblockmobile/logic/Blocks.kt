package com.apochromat.codeblockmobile

import java.util.*

class Heap {
    private var heap: MutableMap<String, Int> = mutableMapOf()

    fun createDefaultVariables(names: List<String>) {
        for (name: String in names) {
            heap[name] = 0
        }
    }

    fun setVariableValue(name: String, value: Int) { heap[name] = value }

    fun getVariableValue(name: String): Int? { return heap[name] }

    fun isVariableExist(name: String): Boolean { return name in heap.keys }

    fun getVariablesList(): MutableSet<String> { return heap.keys }

    fun deleteVariable(name: String) { heap.remove(name) }

    fun clearVariables() { heap.clear() }
}

open class Block {
    companion object {
        var heap: Heap = Heap()
        var counter: Int = 0
        var allBlocks: MutableMap<Int, Block> = mutableMapOf()
        var strongConnections: MutableList<Pair<Block, Block>> = mutableListOf()
    }

    private var nextBlock: Block? = null
    private var prevBlock: Block? = null
    private var type: String = "Undefined"
    private var status: String = "OK"
    private var id: Int = counter++

    init {
        allBlocks[this.getBlockId()] = this
    }

    fun setBlockType(input: String) { type = input }
    fun getBlockType(): String { return type }
    fun setBlockStatus(input: String) { status = input }
    fun getBlockStatus(): String { return status }

    fun getBlockId(): Int { return id }
    fun getBlockById(_id : Int): Block? { return allBlocks[_id] }
    fun getAllBlocks(): MutableMap<Int, Block> { return allBlocks }
    fun accessHeap(): Heap { return heap }

    fun setNextBlock(block: Block?) { nextBlock = block }
    fun getNextBlock(): Block? { return nextBlock }
    fun setPrevBlock(block: Block?) { prevBlock = block }
    fun getPrevBlock(): Block? { return prevBlock }

    fun addStrongConnection(pair: Pair<Block, Block>) {
        strongConnections.add(pair)
    }
    fun getAllStrongConnections(): MutableList<Pair<Block, Block>> { return strongConnections }

    open fun setBlockData() {}
    open fun clearBlockData() {}

    open fun run() {
        setBlockData()
        if (getNextBlock() == null) {
            println("Program finished with status: ${getBlockStatus()}")
        }
        else if (getBlockStatus() == "OK") {
            getNextBlock()?.run()
        }
        else {
            println("Program finished with status: ${getBlockStatus()}")
        }
    }
}

class DefinedVariable : Block() {
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
    override fun setBlockData() {
        var calculated = arithmetics(accessHeap(), inputValue)
        setBlockStatus(calculated.first)
        name = inputName
        if (calculated.first == "OK") {
            value = calculated.second
            accessHeap().setVariableValue(name, value)
        }
    }
    override fun clearBlockData() {
        value = 0
        name = ""
    }
}

class UndefinedVariable: Block() {
    var inputNames: List<String> = listOf()
    init {
        setBlockType("UndefinedVariable")
    }
    fun setBlockInput(_names: List<String>) {
        inputNames = _names
    }
    override fun setBlockData() {
        accessHeap().createDefaultVariables(inputNames)
    }
}

class Assignment : Block() {
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
    override fun setBlockData() {
        if (accessHeap().isVariableExist(inputName)) {
            var calculated = arithmetics(accessHeap(), inputValue)
            setBlockStatus(calculated.first)
            name = inputName
            if (calculated.first == "OK") {
                value = calculated.second
                accessHeap().setVariableValue(name, value)
            }
        }
        else {
            setBlockStatus("Undefined Variable: $inputName")
        }
    }
    override fun clearBlockData() {
        value = 0
        name = ""
    }
}

class EntryPoint: Block() {
    init {
        setBlockType("EntryPoint")
    }
    override fun setBlockData() {
        accessHeap().clearVariables()
        for (bl in getAllBlocks()) {
            bl.value.clearBlockData()
        }
        for (pair in getAllStrongConnections()) {
            connectBlocks(pair.first, pair.second, false)
        }
    }
}

class BeginEnd : Block() {
    init {
        setBlockType("BeginEnd")
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
    override fun setBlockData() {
        getNextBlock()?.let { connectBlocks(ifEnd, it, true, false) }

        if (expressionComparator !in listOf<String>(">", ">=", "<", "<=", "==", "!=")) {
            setBlockStatus("Invalid Comparator")
        }
        else {
            var calculateLeft = arithmetics(accessHeap(), expressionLeft)
            var calculateRight = arithmetics(accessHeap(), expressionRight)
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
    override fun setBlockData() {
        getNextBlock()?.let { connectBlocks(ifEnd, it, true, false) }
        getNextBlock()?.let { connectBlocks(elseEnd, it, true, false) }

        if (expressionComparator !in listOf<String>(">", ">=", "<", "<=", "==", "!=")) {
            setBlockStatus("Invalid Comparator")
        }
        else {
            var calculateLeft = arithmetics(accessHeap(), expressionLeft)
            var calculateRight = arithmetics(accessHeap(), expressionRight)
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

class ConsoleOutput: Block() {
    private var message: String = ""
    init {
        setBlockType("ConsoleOutput")
    }
    fun setBlockInput( _message: String = "") {
        message = _message
    }
    override fun setBlockData() {
        println(accessHeap().getVariableValue(message).toString())
    }
}

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
    override fun setBlockData() {
        print(message)
        val inputValue: String = readln()
        var calculated = arithmetics(accessHeap(), inputValue)
        setBlockStatus(calculated.first)
        if (calculated.first == "OK") {
            value = calculated.second
            accessHeap().setVariableValue(name, value)
        }
    }
    override fun clearBlockData() {
        value = 0
        name = ""
    }
}

fun connectBlocks(blockFrom: Block, blockTo: Block, strong: Boolean = true, clear: Boolean = true) {
    if (strong) Block().addStrongConnection(Pair(blockFrom, blockTo))
    if (clear) {
        blockFrom.getNextBlock()?.setPrevBlock(null)
        blockTo.getPrevBlock()?.setNextBlock(null)
    }
    blockFrom.setNextBlock(blockTo)
    blockTo.setPrevBlock(blockFrom)
}

fun disconnectBlocks(blockFrom: Block, blockTo: Block) {
    blockFrom.setNextBlock(null)
    blockTo.setPrevBlock(null)
}


fun arithmetics(heap: Heap, expression: String): Pair<String, Int> {
    var exp = expression.replace("\\s".toRegex(), "");
    if(exp.length==0) {
        return Pair("Empty Input", 0);
    }
    val (prepered, expStatus) = preperingExpression(heap, exp);
    val (correctLine, lineStatus) = lineСheck(exp);
    if (expStatus == 0) {
        return Pair(prepered, 0)
    }
    if (lineStatus == 0) {
        return Pair(correctLine, 0)
    }

    return RPNToAnswer(ExpressionToRPN(prepered))
}

fun GetPriority(token: Char): Int {
    when (token) {
        '*', '/','%' -> return 3;
        '+', '-' -> return 2;
        '(' -> return 1;
        ')' -> return -1;
        else -> return 0;
    }
}

fun ExpressionToRPN(expression: String): String {
    var current = "";
    var stack: Stack<Char> = Stack<Char>();
    var priority: Int;
    for (i in expression.indices) {
        priority = GetPriority(expression[i]);
        when (priority) {
            0 -> current += expression[i];
            1 -> stack.push(expression[i]);
            2, 3 -> {
                current += " ";
                while (!stack.empty()) {
                    if ((GetPriority(stack.peek()) >= priority)) current += stack.pop();
                    else break;
                }
                stack.push(expression[i]);
            }
            -1 -> {
                current += " ";
                while (GetPriority(stack.peek()) != 1) current += stack.pop();
                stack.pop();
            }
            else -> {
                return "Error";
            }
        }
    }
    while (!stack.empty()) current += stack.pop();
    return current;
}

fun RPNToAnswer(rpn: String): Pair<String,Int> {
    var operand = String();
    var stack: Stack<Int> = Stack<Int>();
    var i = 0;
    while (i < rpn.length) {
        if (rpn[i] == ' ') {
            i++;
            continue;
        }
        if (GetPriority(rpn[i]) == 0) {
            while (rpn[i] !=' ' && (GetPriority(rpn[i]) == 0)) {
                operand += rpn[i++];
                if (i == rpn.length) break;
            }
            try {
                stack.push(operand.toInt());
            }catch (e:NumberFormatException){
                return Pair("Unexpected Symbol", 0)
            }
            operand = String();
        }
        if (i == rpn.length) break;
        if (GetPriority(rpn[i]) > 1) {
            try{
            var a: Int = stack.pop();
            var b: Int = stack.pop();
            when (rpn[i]) {
                '+' -> stack.push(b + a);
                '-' -> stack.push(b - a);
                '*' -> stack.push(b * a);
                '/' ->{
                        try {
                            stack.push(b / a);
                        } catch (e:Exception){
                            return Pair ("Division By Zero", 0);
                        }
                    }
                '%' -> stack.push(b % a);
                else -> {
                    println(stack.pop())
                    return Pair("Unexpected Symbol", 0);
                }
                }
            } catch (e:EmptyStackException){
            return Pair("Incorrect Expression",0);
            }
        }
        i++;
    }
    return Pair("OK", stack.pop());
}

fun lineСheck (string:String): Pair<String,Int>{
    var str = string.replace("[A-Za-z-+*/0-9()%]".toRegex(),"")
    if(str.length != 0){
        return Pair("Unexpected Symbol", 0);
    }
    val reg="[A-Za-z]+[0-9]|[0-9][A-Za-z]".toRegex();
    val match=reg.find(string);
    if (match != null) {
        return Pair("Incorrect Expression",0)
    }
    return Pair("OK", 1);
}
fun preperingExpression (heap: Heap,expression:String):Pair<String,Int> {
    var exp = expression;
    var preperedExpression = String();
    var i = 0;
    while (i < expression.length) {
        if(expression[i].code >= 65 && expression[i].code <= 127){
            var operand = String();
            while (expression[i] != ' ' && (expression[i].code >= 65 && expression[i].code <= 127)){
                operand += expression[i++];
                if (i == expression.length) break;
            }
            if(!heap.isVariableExist(operand)){
                return Pair("Undefined Variable: ${operand}", 0);
            }
            println(operand);
            var FromVarToNum = heap.getVariableValue(operand);

            exp = expression.replace(operand, FromVarToNum.toString());

        }
        i++;
    }

    for(j in exp.indices) {
        if(exp[j] == '-') {
            if(j == 0){
                preperedExpression += "0";
            }
            else if(exp[j-1] == '('){
                preperedExpression += "0";
            }
        }
        preperedExpression += exp[j];
    }

    return Pair(preperedExpression, 1);
}
fun VariableСheck(variable: String): Boolean{
    var str = variable.replace("[A-Za-z]".toRegex(),"")
    if(str.length != 0){
       return false;
    }
    return true;
}