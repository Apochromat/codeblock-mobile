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
    lateinit var adapterConsole: ConsoleAdapter
    lateinit var adapterBlocks: BlocksAdapter
    lateinit var holder: BlocksAdapter.ViewHolder
    var activity: ProjectActivity? = null

    // Флаг. В while, if, if-else заставляет выполниться initVar() единожды
    // т.к. он перезаписывает блоки выхода Exit
    var flagInit = true

    //  Ссылки на следующий и предыдущий блоки
    var nextBlock: Block? = null
    var prevBlock: Block? = null

    //  Тип, статус и идентификатор блока
    var type: String = ""
    var status: String = ok()

    /**
     * Функция, которая дает доступ к хранилищу переменных
     **/
    fun accessHeap(): Heap {
        return heap
    }

    /**
     * Функция, которая инициализирует входные данные из полей
     **/
    open fun initVar() {
    }
    /**
     * Функция, которая исполняет внутреннюю логику блока
     **/
    open fun executeBlock() {
        status = ok()
    }

    /**
     * Функция, которая запускает выполнение очередного блока
     **/
    open fun kickRunning() {}

    open fun run() {
        // Отдельно работаем с выполнением ConsoleInput т.к. он ставит выполнение на паузу
        if (type == "ConsoleInput") {
            executeBlock()
        } else {
            executeBlock()
            when {
                // При конце выполнения программы опускаем флаг, разъединяем все блоки, выводим статус
                nextBlock == null -> {
                    isProgramRunning = false
                    activity?.disconnectAllBlocks()
                    adapterConsole.addMessage(programFinish(status))
                    adapterBlocks.notifyItemChanged(indexListBlocks)
                }
                status == ok() -> {
                    callStack.push(nextBlock)
                }
                // Если произошла какая-то ошибка при выполнении
                else -> {
                    isProgramRunning = false
                    activity?.disconnectAllBlocks()
                    adapterConsole.addMessage(programFinish(status))
                    adapterBlocks.notifyItemChanged(indexListBlocks)
                }
            }
        }
    }
}