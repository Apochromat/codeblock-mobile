package com.apochromat.codeblockmobile.logic

fun main() {
    var ep = EntryPoint()
    var block1 = Block()
    var block2 = DefinedVariable()
    var block3 = UndefinedVariable()
    var block4 = Assignment()

    block2.setBlockInput("tempVar", "10")
    block3.setBlockInput(listOf("Tom", "Sam", "Kate", "Bob", "Alice"))
    block4.setBlockInput("Alice", "12345")

    connectBlocks(ep, block1)
    connectBlocks(block1, block2)
    connectBlocks(block2, block3)
    connectBlocks(block3, block4)
    ep.run()

    println(Block().accessHeap().getVariablesList())
    println(Block().accessHeap().getVariableValue("tempVar"))
    println(Block().accessHeap().getVariableValue("Tom"))
    println(Block().accessHeap().getVariableValue("Alice"))
}