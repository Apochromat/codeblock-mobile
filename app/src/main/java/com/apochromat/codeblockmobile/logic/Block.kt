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
    var nextBlock: Block? = null
    var prevBlock: Block? = null

    //  Тип, статус и идентификатор блока
    var type: String = ""
    var status: String = ok()

    //  Получить доступ к хранилищу переменных
    fun accessHeap(): Heap {
        return heap
    }

    open fun executeBlock() {
        status = ok()
    }
    open fun kickRunning() {}

    open fun run() {
        if (type == "ConsoleInput"){
            executeBlock()
        }
        else{
            executeBlock()
            when {
                nextBlock == null -> {
                    isProgramRunning = false
                    adapterConsole.addMessage(programFinish(status))
                    adapterBlocks.notifyItemChanged(indexListBlocks)
                }
                status == ok() -> {
                    callStack.push(nextBlock)
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