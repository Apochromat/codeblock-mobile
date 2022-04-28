package com.apochromat.codeblockmobile.logic

/**
 *  Блок цикла While.
 *  Позволяет выполнять определенный набор команд при выполнении условия.
 **/
class CycleWhile : Block() {
    private var expressionLeft: String = ""
    private var expressionRight: String = ""
    private var expressionComparator: String = ">="
    var cycleBegin: BeginEnd = BeginEnd()
    var cycleEnd: BeginEnd = BeginEnd()
    private var cycleExit: BeginEnd = BeginEnd()

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
    }

    override fun executeBlock() {
        initVar()
        connectBlocks(cycleEnd, this, strong = false, clear = false)
        getNextBlock()?.let {
            if (getNextBlock() != cycleBegin)
                connectBlocks(cycleExit, it, strong = true, clear = false)
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
                connectBlocks(this, cycleBegin, strong = false, clear = false)
            } else {
                connectBlocks(this, cycleExit, strong = false, clear = false)
            }
            return
        }
        setBlockStatus(if(calculateLeft.first == "OK") calculateRight.first else calculateLeft.first)
    }
}