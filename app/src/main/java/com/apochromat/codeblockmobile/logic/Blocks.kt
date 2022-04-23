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

    fun clearVariables() {heap.clear()}
}

open class Block {
    companion object {
        var heap: Heap = Heap()
        var counter: Int = 0
        var allBlocks: MutableMap<Int, Block> = mutableMapOf()
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

    open fun setBlockData() {}
    open fun clearBlockData() {}

    open fun run() {
        setBlockData()
        if (getBlockStatus() == "OK") getNextBlock()?.run()
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
            setBlockStatus("ERROR: Undefined Variable: $inputName")
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
    }
}

class Condition: Block() {

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

fun connectBlocks(blockFrom: Block, blockTo: Block) {
    blockFrom.getNextBlock()?.setPrevBlock(null)
    blockTo.getPrevBlock()?.setNextBlock(null)
    blockFrom.setNextBlock(blockTo)
    blockTo.setPrevBlock(blockFrom)
}

fun disconnectBlocks(blockFrom: Block, blockTo: Block) {
    blockFrom.setNextBlock(null)
    blockTo.setPrevBlock(null)
}

fun arithmetics(heap: Heap, expression: String): Pair<String, Int> {
    return Pair("OK", RPNToAnswer(ExpressionToRPN(heap,expression)))
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

fun ExpressionToRPN(heap: Heap, expression: String): String {
    var current: String = "";
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

fun RPNToAnswer(rpn: String): Int {
    var operand: String = String();
    var stack: Stack<Int> = Stack<Int>();
    var i: Int = 0;
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
            stack.push(operand.toInt());
            operand = String();
        }
        if (i == rpn.length) break;
        if (GetPriority(rpn[i]) > 1) {
            var a: Int = stack.pop();
            var b: Int = stack.pop();
            when (rpn[i]) {
                '+' -> stack.push(b + a);
                '-' -> stack.push(b - a);
                '*' -> stack.push(b * a);
                '/' -> stack.push(b / a);
                '%' -> stack.push(b % a);
                else -> {
                    return -1;
                }
            }
        }
        i++;
    }
    return stack.pop();
}