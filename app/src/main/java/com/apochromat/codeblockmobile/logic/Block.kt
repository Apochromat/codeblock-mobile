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
    private var type: String = ""
    var status: String = ok()

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

    open fun executeBlock() {
        status = ok()
    }
    open fun kickRunning() {}

    open fun run() {
        if (getBlockType() == "ConsoleInput"){
            executeBlock()
        }
        else{
            executeBlock()
            when {
                getNextBlock() == null -> {
                    isProgramRunning = false
                    adapterConsole.addMessage(programFinish(status))
                    adapterBlocks.notifyItemChanged(indexListBlocks)
                }
                getBlockStatus() == ok() -> {
                    callStack.push(getNextBlock())
                }
                else -> {
                    isProgramRunning = false
                    adapterConsole.addMessage(programFinish(status))
                    adapterBlocks.notifyItemChanged(indexListBlocks)
                }
            }
        }
    }
}