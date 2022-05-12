package com.apochromat.codeblockmobile.logic

fun main() {
    val ep = EntryPoint()
    val da = UndefinedArray()
    val uv = DefinedVariable()
    val dv=DefinedVariable()
    val vass = Assignment()
    val ass = Assignment()
    val wh = CycleWhile()
    val out = ConsoleOutput()
    val vout = ConsoleOutput()
    dv.setBlockInput("var","1")
    Block().accessHeap().createArray("Pupsich", 1337)
    Block().accessHeap().setArrayValue("Pupsich",1,5)
    connectBlocks(ep,dv)
    connectBlocks(dv,out)
    ep.run()
    println(Block().accessHeap().isVariableExist("var"))
    println(defineInput(Block().accessHeap(),"var"))
}