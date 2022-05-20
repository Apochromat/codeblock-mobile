package com.apochromat.codeblockmobile.logic

/**
 *  Блок условия IfElse.
 *  Позволяет выполнять определенные наборы команд при выполнении и не выполнении условия.
 **/
class ConditionIfElse : Block() {
    private var expressionLeft: String = ""
    private var expressionRight: String = ""
    private var expressionComparator: String = ">="

    init {
        type = "ConditionIfElse"
    }

    private fun initVar(){
        expressionLeft = inputLeftEdit
        expressionRight = inputRightEdit
        expressionComparator = inputComparator

        begin.adapterConsole = this.adapterConsole
        end.adapterConsole = this.adapterConsole
        beginElse.adapterConsole = this.adapterConsole
        endElse.adapterConsole = this.adapterConsole
        exit = Exit()
        exit.adapterConsole = this.adapterConsole
        begin.adapterBlocks = this.adapterBlocks
        end.adapterBlocks = this.adapterBlocks
        exit.adapterBlocks = this.adapterBlocks
        beginElse.adapterBlocks = this.adapterBlocks
        endElse.adapterBlocks = this.adapterBlocks

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

    override fun executeBlock() {
        super.executeBlock()
        if (crutch) initVar()
        connectBlocks(end, exit, strong = true, clear = false)
        connectBlocks(endElse, exit, strong = true, clear = false)

        nextBlock?.let {
            if (nextBlock != begin && nextBlock != beginElse && nextBlock != exit &&
                nextBlock != end && nextBlock != endElse && nextBlock != null)
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
                connectBlocks(this, begin, false, false)
            } else {
                connectBlocks(this, beginElse, false, false)
            }
            return
        }
        status = if(calculateLeft.first == ok()) calculateRight.first else calculateLeft.first
    }
}