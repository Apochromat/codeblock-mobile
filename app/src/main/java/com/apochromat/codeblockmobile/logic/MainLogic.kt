package com.apochromat.codeblockmobile

fun main() {
    var ep = EntryPoint()
    var block1 = Block()
    var block2 = DefinedVariable()
    var block3 = UndefinedVariable()
    var block4 = Assignment()
    var block5 = Assignment()

    block2.setBlockInput("tempVar", "10")
    block3.setBlockInput(listOf("Tom", "Sam", "Kate", "Bob", "Alice"))
    block4.setBlockInput("Alice", "12345")
    block5.setBlockInput("Alice", "64+10")

    connectBlocks(ep, block1)
    connectBlocks(block1, block2)
    connectBlocks(block2, block3)
    connectBlocks(block3, block4)
    ep.run()

    println(Block().accessHeap().getVariablesList())
    println(Block().accessHeap().getVariableValue("tempVar"))
    println(Block().accessHeap().getVariableValue("Tom"))
    println(Block().accessHeap().getVariableValue("Alice"))

    connectBlocks(block4, block5)
    ep.run()

    println(Block().accessHeap().getVariableValue("Alice"))
}