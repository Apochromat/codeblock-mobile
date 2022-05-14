package com.apochromat.codeblockmobile.logic

import com.apochromat.codeblockmobile.BlocksAdapter
import com.apochromat.codeblockmobile.ConsoleAdapter
import com.apochromat.codeblockmobile.ProjectActivity
import java.util.*

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

        // Флаг работы программы
        var isProgramRunning = false
    }
    // Переменные с данными

    var inputLeftEdit: String = ""
    var inputMediumEdit: String = ""
    var inputRightEdit: String = ""
    var inputComparator: String = ">="
    var indexComparator: Int = 0
    var valueVar: String = ""
  
    lateinit var begin: Begin
    lateinit var end: End
    lateinit var exit: Exit
    lateinit var beginElse: Begin
    lateinit var endElse: End

    var indexListBlocks = 0
    lateinit var adapterConsole : ConsoleAdapter
    lateinit var adapterBlocks : BlocksAdapter
    lateinit var holder : BlocksAdapter.ViewHolder
    lateinit var activity: ProjectActivity

    var crutch = true
    //  Ссылки на следующий и предыдущий блоки
    private var nextBlock: Block? = null
    private var prevBlock: Block? = null

    //  Тип, статус и идентификатор блока
    private var type: String = "Undefined"
    var status: String = "OK"
    private var id: Int = counter++

    init {
        allBlocks[this.getBlockId()] = this
    }

    //  Операции с типом, статусом и идентификатором блока
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

    fun getBlockById(_id: Int): Block? {
        return allBlocks[_id]
    }

    //  Получить словарь всех блоков
    fun getAllBlocks(): MutableMap<Int, Block> {
        return allBlocks
    }

    //  Получить доступ к хранилищу переменных
    fun accessHeap(): Heap {
        return heap
    }

    //  Операции с ссылками на предыдущий и следующий блоки
    fun setNextBlock(block: Block?) {
        nextBlock = block
    }

    fun getNextBlock(): Block? {
        return nextBlock
    }

    fun setPrevBlock(block: Block?) {
        prevBlock = block
    }

    fun getPrevBlock(): Block? {
        return prevBlock
    }

    //  Операции со списком устойчивых связей между блоками
    fun addStrongConnection(pair: Pair<Block, Block>) {
        strongConnections.add(pair)
    }

    fun removeStrongConnection(pair: Pair<Block, Block>) {
        strongConnections.remove(pair)
    }

    fun getAllStrongConnections(): MutableList<Pair<Block, Block>> {
        return strongConnections
    }

    open fun executeBlock() {}
    open fun kickRunning() {}
    fun clearBlockData() {
        status = "OK"
    }

    open fun run() {
//        adapterConsole.addMessage( getPrevBlock()?.indexListBlocks.toString() + " " + type + " " + getNextBlock()?.indexListBlocks.toString())
        if (getBlockType() == "ConsoleInput"){
            executeBlock()
        }
        else{
            executeBlock()
            when {
                getNextBlock() == null -> {
                    isProgramRunning = false
                    println("Program finished with status: ${getBlockStatus()}")
                    adapterConsole.addMessage("Program finished with status: ${getBlockStatus()}")
                    adapterBlocks.notifyItemChanged(indexListBlocks)
                }
                getBlockStatus() == "OK" -> {
                    callStack.push(getNextBlock())
                }
                else -> {
                    isProgramRunning = false
                    println("Program finished with status: ${getBlockStatus()}")
                    adapterConsole.addMessage("Program finished with status: ${getBlockStatus()}")
                    adapterBlocks.notifyItemChanged(indexListBlocks)
                }
            }
        }
    }
}