package com.apochromat.codeblockmobile.logic

/**
 *  Блок цикла While.
 *  Позволяет выполнять определенный набор команд при выполнении условия.
 **/
class CycleWhile : Block() {
    private var expressionLeft: String = ""
    private var expressionRight: String = ""
    private var expressionComparator: String = ">="

    init {
        type = "CycleWhile"
    }

    fun setBlockInput(
        _expressionLeft: String,
        _expressionRight: String,
        _expressionComparator: String = ">="
    ) {
        expressionLeft = _expressionLeft
        expressionRight = _expressionRight
        expressionComparator = _expressionComparator
    }

    override fun initVar(){
        expressionLeft = inputLeftEdit
        expressionRight = inputRightEdit
        expressionComparator = inputComparator

        begin.adapterConsole = this.adapterConsole
        end.adapterConsole = this.adapterConsole
        exit = Exit()
        exit.adapterConsole = this.adapterConsole
        begin.adapterBlocks = this.adapterBlocks
        end.adapterBlocks = this.adapterBlocks
        exit.adapterBlocks = this.adapterBlocks
        flagInit = false
    }

    override fun executeBlock() {
        super.executeBlock()
        // Выполняем initVar() единожды
        if (flagInit) initVar()
        // Соединяем блок конца при выполнении условия с выходом - блоком которым соединен while блок
        connectBlocks(end, this, clear = false)

        // Соединяем выход с блоком, после if, если это не блок логики if
        nextBlock?.let {
            if (nextBlock != begin && nextBlock != exit && nextBlock != end && nextBlock != null)
                connectBlocks(exit, it, clear = false)
        }

        // Проверяем правильность операторов сравнения
        if (expressionComparator !in allComparators) {
            status = invalidComparator()
            return
        }

        // Высчитываем левую и правую часть для сравнения
        val calculateLeft = arithmetics(accessHeap(), expressionLeft)
        val calculateRight = arithmetics(accessHeap(), expressionRight)
        // Проверяем правильность вычислений
        if ((calculateLeft.first != ok()) || (calculateRight.first != ok())) {
            status = if (calculateLeft.first == ok()) calculateRight.first else calculateLeft.first
            return
        }
        // Сравниваем
        if (expressionComparator(
                calculateLeft.second,
                calculateRight.second,
                expressionComparator
            )
        ) {
            connectBlocks(this, begin, clear = false)
        } else {
            connectBlocks(this, exit, clear = false)
        }
    }
}