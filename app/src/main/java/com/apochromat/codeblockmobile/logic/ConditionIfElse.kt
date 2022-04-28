package com.apochromat.codeblockmobile.logic

/**
 *  Блок условия IfElse.
 *  Позволяет выполнять определенные наборы команд при выполнении и не выполнении условия.
 **/
class ConditionIfElse : Block() {
    private var expressionLeft: String = ""
    private var expressionRight: String = ""
    private var expressionComparator: String = ">="
    var ifBegin: BeginEnd = BeginEnd()
    var ifEnd: BeginEnd = BeginEnd()
    var elseBegin: BeginEnd = BeginEnd()
    var elseEnd: BeginEnd = BeginEnd()
    private var conditionExit: BeginEnd = BeginEnd()

    init {
        setBlockType("ConditionIfElse")
    }
    private fun initVar(){
        expressionLeft = inputLeftEdit
        expressionRight = inputRightEdit
        expressionComparator = inputComparator
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
        initVar()
        connectBlocks(ifEnd, conditionExit, strong = true, clear = false)
        connectBlocks(elseEnd, conditionExit, strong = true, clear = false)
        getNextBlock()?.let {
            if (getNextBlock() != ifBegin && getNextBlock() != elseBegin)
                connectBlocks(conditionExit, it, strong = true, clear = false)
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
                connectBlocks(this, ifBegin, false)
            } else {
                connectBlocks(this, elseBegin, false)
            }
            return
        }
        setBlockStatus(if(calculateLeft.first == "OK") calculateRight.first else calculateLeft.first)
    }
}