package com.apochromat.codeblockmobile.logic

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

    open fun run() {
        setBlockData()
        nextBlock?.run()
    }
}

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
    override fun setBlockData() {
        if (inputValue != ""){
            try {
                value = inputValue.toInt()
                setBlockStatus("OK")
            } catch (e: NumberFormatException) { setBlockStatus("ERROR: Incorrect Number") }
        }
        name = inputName
        accessHeap().setVariableValue(name, value)
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
    override fun setBlockData() {
        if (accessHeap().isVariableExist(inputName)) {
            if (inputValue != "") {
                try {
                    value = inputValue.toInt()
                    setBlockStatus("OK")
                } catch (e: NumberFormatException) { setBlockStatus("ERROR: Incorrect Number") }
            }
            name = inputName
            accessHeap().setVariableValue(name, value)
        }
        else {
            setBlockStatus("ERROR: Undefined Variable: $inputName")
        }
    }
}

class EntryPoint: Block() {
    init {
        setBlockType("EntryPoint")
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
    return Pair("Status", 100)
}