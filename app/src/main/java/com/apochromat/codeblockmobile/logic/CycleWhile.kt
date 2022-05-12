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
        setBlockType("CycleWhile")
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
    }

    override fun executeBlock() {
        initVar()
        connectBlocks(end, this, strong = false, clear = false)
        getNextBlock()?.let {
            if (getNextBlock() != begin)
                connectBlocks(exit, it, strong = true, clear = false)
        }

        if (expressionComparator !in listOf(">", ">=", "<", "<=", "==", "!=")) {
            setBlockStatus("Invalid comparator")
            return
        }
        val calculateLeft = arithmetics(accessHeap(), expressionLeft)
        val calculateRight = arithmetics(accessHeap(), expressionRight)
        if ((calculateLeft.first == "OK") && (calculateRight.first == "OK")) {
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
        setBlockStatus(if(calculateLeft.first == "OK") calculateRight.first else calculateLeft.first)
    }
}