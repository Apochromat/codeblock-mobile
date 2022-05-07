package com.apochromat.codeblockmobile.logic

/**
 *  Точка входа в программу.
 **/
class EntryPoint : Block() {
    init {
        setBlockType("EntryPoint")
    }

    override fun executeBlock() {
        accessHeap().clearVariables()
        for (bl in getAllBlocks()) {
            bl.value.clearBlockData()
        }
//        for (pair in getAllStrongConnections()) {
//            connectBlocks(pair.first, pair.second, false)
//        }
    }

    override fun run() {
        super.run()
        while (!callStack.empty()) {
            callStack.pop().run()
        }
    }
}