package com.apochromat.codeblockmobile.logic
import blocks.*

fun main() {
    var block = Block()
    var brick = DefinedVariable()
    var brack = UndefinedVariable()
    var brock = Assignment()

    brick.setBlockData("tempVar", "-10")
    brack.setBlockData(listOf("Tom", "Sam", "Kate", "Bob", "Alice"))

    brock.setBlockData("Alice", "12345")

    println("ID:${block.getBlockId()} Type:${block.getBlockType()}")
    println("ID:${brick.getBlockId()} Type:${brick.getBlockType()} Value:${brick.getBlockValue()}" +
            " Status:${brick.getBlockStatus()}")
    println(block.getAllBlocks()[1].getBlockType())
    println(block.accessHeap().getVariablesList())
    println(Block().accessHeap().isVariableExist("Tomas"))
    println(Block().accessHeap().getVariableValue("Alice"))
}