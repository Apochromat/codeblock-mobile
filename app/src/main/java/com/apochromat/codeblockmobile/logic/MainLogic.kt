package com.apochromat.codeblockmobile.logic

fun main() {
    var ep = EntryPoint()
    var block1 = Block()
    var block2 = DefinedVariable()
    var block3 = UndefinedVariable()
    var block4 = Assignment()
    var block5 = ConsoleInputOne()
    var block6 = ConsoleOutput()

    block2.setBlockInput("tempVar", "-10")
    block3.setBlockInput(listOf("Tom", "Sam", "Kate","Alice" ))
    block4.setBlockInput("Alice", "4")
 //   block5.setBlockInput("consoleVar", "Write new var ")
//    block6.setBlockInput("consoleVar")

    connectBlocks(ep, block1)
    connectBlocks(block1, block2)
    connectBlocks(block2, block3)
    connectBlocks(block3, block4)
//    connectBlocks(block4, block5)
//    connectBlocks(block5, block6)
   //  disconnectBlocks(brack, brock)
    ep.run()

//    println("ID:${block1.getBlockId()} Type:${block1.getBlockType()}")
//    println(block1.getBlockById(2)?.getBlockType())
//    println(block1.accessHeap().getVariablesList())
    println(Block().accessHeap().getVariableValue("Alice"))
    println(arithmetics(Block().accessHeap(),"     "))
}