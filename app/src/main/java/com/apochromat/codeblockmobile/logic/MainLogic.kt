package com.apochromat.codeblockmobile.logic
fun main() {
    val ep = EntryPoint()
    val da = UndefinedArray()
    val uv = DefinedVariable()
    val vass = Assignment()
    val ass = Assignment()
    val wh = CycleWhile()
    val out = ConsoleOutput()
    val vout = ConsoleOutput()

    da.setBlockInput("arr", "5")
    out.setBlockInput("arr[5]")
    vout.setBlockInput("var")
    uv.setBlockInput("var", "0")
    vass.setBlockInput("var", "var+1")
    ass.setBlockInput("arr[var]", "var")
    wh.setBlockInput("var", "2", "<")

    connectBlocks(ep, da)
    connectBlocks(da, uv)
    connectBlocks(uv, wh)
    connectBlocks(wh.begin, vass)
    connectBlocks(vass, ass)
    connectBlocks(ass, vout)
    connectBlocks(vout, wh.end)
    connectBlocks(wh, out)

    ep.run()
    println(ep.accessHeap().getArrayValue("arr", 2))
}