package com.apochromat.codeblockmobile.logic

/**
 *  Точка входа в программу.
 **/
class EntryPoint : Block() {
    init {
        type = "EntryPoint"
    }

    override fun executeBlock() {
        accessHeap().clearVariables()
        accessHeap().clearArrays()
    }

    override fun kickRunning() {
        super.kickRunning()
        if (!isProgramRunning) return
        if (callStack.empty()) return
        callStack.pop().run()
    }
}