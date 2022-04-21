package blocks

import java.util.*

class Heap {
    private var heap: MutableMap<String, Int> = mutableMapOf()

    fun createDefaultVariables(names: List<String>) {
        for (name: String in names) {
            heap[name] = 0
        }
    }

    fun setVariableValue(name: String, value: Int) {
        heap[name] = value
    }

    fun getVariableValue(name: String): Int? {
        return heap[name]
    }

    fun isVariableExist(name: String): Boolean {
        return name in heap.keys
    }

    fun getVariablesList(): MutableSet<String> {
        return heap.keys
    }

    fun deleteVariable(name: String) {
        heap.remove(name)
    }

}

open class Block {
    companion object {
        var heap: Heap = Heap()
        var counter: Int = 0
        var allBlocks: MutableList<Block> = mutableListOf()
    }

    private var nextBlock: Block? = null
    private var prevBlock: Block? = null
    private var type: String = "Undefined"
    private var status: String = "OK"
    private var id: Int = counter++

    init {
        allBlocks.add(this)
    }

    fun setBlockType(input: String) {
        type = input
    }

    fun getBlockType(): String {
        return type
    }

    fun setBlockStatus(input: String) {
        status = input
    }

    fun getBlockStatus(): String {
        return status
    }

    fun getBlockId(): Int {
        return id
    }

    fun getAllBlocks(): MutableList<Block> {
        return allBlocks
    }

    fun accessHeap(): Heap {
        return heap
    }

    fun setNextBlock(block: Block) {
        nextBlock = block
    }

    fun getNextBlock(): Block? {
        return nextBlock
    }

    fun setPrevBlock(block: Block) {
        prevBlock = block
    }

    fun getPrevBlock(): Block? {
        return prevBlock
    }

    open fun work() {}
}

class DefinedVariable : Block() {
    private var value: Int = 0
    private var name: String = "none"

    init {
        setBlockType("DefinedVariable")
    }

    fun setBlockData(_name: String, _value: String) {
        if (_value != "") {
            try {
                value = _value.toInt()
                setBlockStatus("OK")
            } catch (e: NumberFormatException) {
                setBlockStatus("ERROR: Incorrect Number")
            }
        }
        name = _name
        accessHeap().setVariableValue(name, value)
    }

    fun getBlockValue(): Int {
        return value
    }
}

class UndefinedVariable : Block() {
    init {
        setBlockType("UndefinedVariable")
    }

    fun setBlockData(names: List<String>) {
        accessHeap().createDefaultVariables(names)
    }
}

class Assignment : Block() {
    private var value: Int = 0
    private var name: String = "none"

    init {
        setBlockType("Assignment")
    }

    fun setBlockData(_name: String, _value: String) {
        if (_value != "") {
            try {
                value = _value.toInt()
                setBlockStatus("OK")
            } catch (e: NumberFormatException) {
                setBlockStatus("ERROR: Incorrect Number")
            }
        }
        name = _name
        accessHeap().setVariableValue(name, value)
    }
}

fun arithmetics(heap: Heap, expression: String): Pair<String, Int> {
    return Pair("Status", RPNToAnswer(ExpressionToRPN(heap,expression)))
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
    while (i != rpn.length) {
        if (rpn[i] == ' ') {
            i++;
            continue;
        }
        if (GetPriority(rpn[i]) == 0) {
            while (rpn[i] != ' ' && (GetPriority(rpn[i]) == 0)) {
                operand += rpn[i++];
                if (i == rpn.length) break;
                stack.push(operand.toInt());
                operand = String();
            }
        }
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