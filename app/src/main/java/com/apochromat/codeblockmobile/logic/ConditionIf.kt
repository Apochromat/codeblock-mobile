package com.apochromat.codeblockmobile.logic

/**
 *  Блок условия If.
 *  Позволяет выполнять определенный набор команд при выполнении условия.
 **/
class ConditionIf : Block() {
    private var expressionLeft: String = ""
    private var expressionRight: String = ""
    private var expressionComparator: String = ">="

    init {
        type = "ConditionIf"
    }
   private fun initVar() {
     
       expressionLeft = inputLeftEdit
       expressionRight = inputRightEdit
       expressionComparator = inputComparator

        begin.adapterConsole = adapterConsole
        end.adapterConsole = adapterConsole
        exit = Exit()
        exit.adapterConsole = adapterConsole
        begin.adapterBlocks = this.adapterBlocks
        end.adapterBlocks = this.adapterBlocks
        exit.adapterBlocks = this.adapterBlocks
       crutch = false
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
                connectBlocks(this, begin, false, false)
            } else {
                connectBlocks(this, end, false, false)
            }
            return
        }
        status = if(calculateLeft.first == ok()) calculateRight.first else calculateLeft.first
    }
}