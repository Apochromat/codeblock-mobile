package com.apochromat.codeblockmobile.logic

/**
 *  Блок условия IfElse.
 *  Позволяет выполнять определенные наборы команд при выполнении и не выполнении условия.
 **/
class ConditionIfElse : Block() {
    private var expressionLeft: String = ""
    private var expressionRight: String = ""
    private var expressionComparator: String = ">="
    private var conditionExit: Exit = Exit()

    init {
        setBlockType("ConditionIfElse")
    }
    private fun initVar(){
        expressionLeft = inputLeftEdit
        expressionRight = inputRightEdit
        expressionComparator = inputComparator

        begin.adapter = this.adapter
        end.adapter = this.adapter
        beginElse.adapter = this.adapter
        endElse.adapter = this.adapter
        conditionExit.adapter = this.adapter
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
        connectBlocks(end, conditionExit, strong = true, clear = false)
        connectBlocks(endElse, conditionExit, strong = true, clear = false)
        getNextBlock()?.let {
            if (getNextBlock() != begin && getNextBlock() != beginElse)
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
                connectBlocks(this, begin, false)
            } else {
                connectBlocks(this, beginElse, false)
            }
            return
        }
        setBlockStatus(if(calculateLeft.first == "OK") calculateRight.first else calculateLeft.first)
    }
}