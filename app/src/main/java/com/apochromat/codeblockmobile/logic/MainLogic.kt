package com.apochromat.codeblockmobile.logic

fun main() {
    val ep = EntryPoint()
    val da = UndefinedArray()
    val uv = UndefinedVariable()
    val vass = Assignment()
    val ass = Assignment()
    val wh = CycleWhile()
    val out = ConsoleOutput()
    val vout = ConsoleOutput()

    da.setBlockInput("arr", "5")
    out.setBlockInput("arr[4]")
    vout.setBlockInput("var")
    uv.setBlockInput("var")
    vass.setBlockInput("var", "var+1")
    ass.setBlockInput("arr[var-1]", "var")
    wh.setBlockInput("var", "4", "<=")

    connectBlocks(ep, da)
    connectBlocks(da, uv)
    connectBlocks(uv, wh)
    connectBlocks(wh.begin, vass)
    connectBlocks(vass, ass)
    connectBlocks(ass, wh.end)
    connectBlocks(wh, out)

    ep.run()
    println(ep.accessHeap().getArrayValue("arr", 2))
}