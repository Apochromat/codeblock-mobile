package com.apochromat.codeblockmobile.logic

fun main() {
    val ep = EntryPoint()
    val da = DefinedArray()
    val out = ConsoleOutput()

    da.setBlockInput("arr", "10, arr[0]+1, arr[1]+ 1, arr[2]+1, arr[3]+1", "5")
    out.setBlockInput("arr[5]")

    connectBlocks(ep, da)
    connectBlocks(da, out)

    ep.run()
    println(ep.accessHeap().isArrayExist("arr"))
    println(ep.accessHeap().getArrayValue("arr", 2))
}