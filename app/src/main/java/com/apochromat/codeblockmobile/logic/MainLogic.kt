package com.apochromat.codeblockmobile

fun main() {
    var ep = EntryPoint()
    var dv = DefinedVariable()
    var assign = Assignment()
    var cycle = CycleWhile()
    var out = ConsoleOutput()
    var outb = ConsoleOutput()
    var uv = UndefinedVariable()

    dv.setBlockInput("tempVar", "-10")
    uv.setBlockInput("Tom, Alice;  Bob,  August")
    assign.setBlockInput("tempVar", "tempVar + 1")
    cycle.setBlockInput("tempVar", "100", "!=")
    out.setBlockInput("tempVar", "variable")
    outb.setBlockInput("tempVar", "variable end")

    connectBlocks(ep, dv)
    connectBlocks(dv, uv)
    connectBlocks(uv, cycle)
    connectBlocks(cycle.cycleBegin, assign)
    connectBlocks(assign, out)
    connectBlocks(out, cycle.cycleEnd)
    connectBlocks(cycle, outb)

    ep.run()
    println(Block().accessHeap().getVariablesList())
}