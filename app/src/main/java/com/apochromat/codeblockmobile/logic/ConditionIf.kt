package com.apochromat.codeblockmobile.logic

/**
 *  Блок условия If.
 *  Позволяет выполнять определенный набор команд при выполнении условия.
 **/
class ConditionIf : Block() {
    private var expressionLeft: String = ""
    private var expressionRight: String = ""
    private var comparator: String = ">="

    init {
        type = "ConditionIf"
    }

    override fun initVar() {
        expressionLeft = inputLeftEdit
        expressionRight = inputRightEdit
        comparator = inputComparator

        begin.adapterConsole = adapterConsole
        end.adapterConsole = adapterConsole
        exit = Exit()
        exit.adapterConsole = adapterConsole
        begin.adapterBlocks = this.adapterBlocks
        end.adapterBlocks = this.adapterBlocks
        exit.adapterBlocks = this.adapterBlocks
        flagInit = false
    }

    override fun executeBlock() {
        super.executeBlock()
        // Выполняем initVar() единожды
        if (flagInit) initVar()
        // Соединяем блок конца при выполнении условия с выходом - блоком которым соединен if блок
        connectBlocks(end, exit, clear = false)

        // Соединяем выход с блоком, после if, если это не блок логики if
        nextBlock?.let {
            if (nextBlock != begin && nextBlock != exit && nextBlock != end && nextBlock != null)
                connectBlocks(exit, it, clear = false)
        }

        // Проверяем правильность операторов сравнения
        if (comparator !in allComparators) {
            status = invalidComparator()
            return
        }

        // Высчитываем левую и правую часть для сравнения
        val calculateLeft = arithmetics(heap, expressionLeft)
        val calculateRight = arithmetics(heap, expressionRight)
        // Проверяем правильность вычислений
        if ((calculateLeft.first != ok()) || (calculateRight.first != ok())) {
            status = if (calculateLeft.first == ok()) calculateRight.first else calculateLeft.first
            return
        }
        // Сравниваем
        if (expressionComparator(
                calculateLeft.second,
                calculateRight.second,
                comparator
            )
        ) {
            connectBlocks(this, begin, false)
        } else {
            connectBlocks(this, exit, false)
        }
    }
}