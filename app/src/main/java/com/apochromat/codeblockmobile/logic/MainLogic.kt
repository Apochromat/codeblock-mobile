package com.apochromat.codeblockmobile.logic

fun main() {
    val ep = EntryPoint()
    val dv = DefinedVariable()
    val dv2 = DefinedVariable()
    val assign = Assignment()
    val assign2 = Assignment()
    val assign3 = Assignment()
    val cycle = CycleWhile()
    val cycle2 = CycleWhile()
    val out = ConsoleOutput()
    val out2 = ConsoleOutput()

    dv.setBlockInput("i", "9")
    dv2.setBlockInput("j", "0")
    assign.setBlockInput("i", "i + 1")
    assign2.setBlockInput("j", "j + 1")
    assign3.setBlockInput("j", "0")
    cycle.setBlockInput("i", "15", "!=")
    cycle2.setBlockInput("j", "5", "!=")
    out.setBlockInput("i", "i >>")
    out2.setBlockInput("j", "j >>")

    connectBlocks(ep, dv)
    connectBlocks(dv, dv2)
    connectBlocks(dv2, cycle)
    connectBlocks(cycle.cycleBegin, assign)
    connectBlocks(assign, out)
    connectBlocks(out, cycle2)
    connectBlocks(cycle2.cycleBegin, assign2)
    connectBlocks(assign2, out2)
    connectBlocks(out2, cycle2.cycleEnd)
    connectBlocks(cycle2, assign3)
    connectBlocks(assign3, cycle.cycleEnd)

    ep.run()
}