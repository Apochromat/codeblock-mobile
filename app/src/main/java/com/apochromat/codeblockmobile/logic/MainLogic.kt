package com.apochromat.codeblockmobile

fun main() {
    var ep = EntryPoint()
    var dv = DefinedVariable()
    var assign = Assignment()
    var cycle = CycleWhile()
    var out = ConsoleOutput()
    var outb = ConsoleOutput()

    dv.setBlockInput("tempVar", "0")
    assign.setBlockInput("tempVar", "tempVar")
    cycle.setBlockInput("tempVar", "10", "<")
    out.setBlockInput("tempVar", "variable")
    outb.setBlockInput("tempVar", "variable end")

    connectBlocks(ep, dv)
    connectBlocks(dv, cycle)
    connectBlocks(cycle.cycleBegin, assign)
    connectBlocks(assign, out)
    connectBlocks(out, cycle.cycleEnd)
    connectBlocks(cycle, outb)

    ep.run()

}