package com.apochromat.codeblockmobile.logic

fun main() {
    val ep = EntryPoint()
    val da = DefinedArray()
    val out = ConsoleOutput()

    da.setBlockInput("arr", "10, 11, 12, 13, 14", "5")
    out.setBlockInput("arr[0]")

    connectBlocks(ep, da)
    connectBlocks(da, out)

    ep.run()
    println(ep.accessHeap().isArrayExist("arr"))
    println(ep.accessHeap().getArrayValue("arr", 2))
}