package com.apochromat.codeblockmobile.logic

/**
 *  Блок условия If.
 *  Позволяет выполнять определенный набор команд при выполнении условия.
 **/
class ConditionIf : Block() {
    private var expressionLeft: String = ""
    private var expressionRight: String = ""
    private var expressionComparator: String = ">="
    private var conditionExit: Exit = Exit()

    init {
        setBlockType("ConditionIf")
    }
    private fun initVar(){
        expressionLeft = inputLeftEdit
        expressionRight = inputRightEdit
        expressionComparator = inputComparator

        begin.adapter = this.adapter
        end.adapter = this.adapter
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
        getNextBlock()?.let {
            if (getNextBlock() != begin && getNextBlock() != null)
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
                connectBlocks(this, end, false)
            }
            return
        }
        setBlockStatus(if(calculateLeft.first == "OK") calculateRight.first else calculateLeft.first)
    }
}