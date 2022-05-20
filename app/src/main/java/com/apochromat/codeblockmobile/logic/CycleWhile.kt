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

    private fun initVar(){
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
        if (flagInit) initVar()
        connectBlocks(end, this, strong = false, clear = false)
        nextBlock?.let {
            if (nextBlock != begin && nextBlock != exit && nextBlock != end && nextBlock != null)
                connectBlocks(exit, it, strong = true, clear = false)
        }

        if (expressionComparator !in allComparators) {
            status = invalidComparator()
            return
        }
        val calculateLeft = arithmetics(accessHeap(), expressionLeft)
        val calculateRight = arithmetics(accessHeap(), expressionRight)
        if ((calculateLeft.first == ok()) && (calculateRight.first == ok())) {
            if (expressionComparator(
                    calculateLeft.second,
                    calculateRight.second,
                    expressionComparator
                )
            ) {
                connectBlocks(this, begin, strong = false, clear = false)
            } else {
                connectBlocks(this, exit, strong = false, clear = false)
            }
            return
        }
        status = if(calculateLeft.first == ok()) calculateRight.first else calculateLeft.first
    }
}