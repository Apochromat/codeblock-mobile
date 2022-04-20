package com.apochromat.codeblockmobile.logic
import blocks.*

fun main(args: Array<String>) {
    var block = Block()
    var brick = DefinedVariable()
    var brack = UndefinedVariable()

    brick.setBlockData("tempVar", "-10")
    brack.setBlockData(listOf("Tom", "Sam", "Kate", "Bob", "Alice"))

    println("ID:${block.getBlockId()} Type:${block.getBlockType()}")
    println("ID:${brick.getBlockId()} Type:${brick.getBlockType()} Value:${brick.getBlockValue()}" +
            " Status:${brick.getBlockStatus()}")
    println(block.getAllBlocks()[1].getBlockType())
    println(block.accessHeap().getVariablesList())
    println(Block().accessHeap().isVariableExist("Tomas"))
}