package com.apochromat.codeblockmobile.logic

/**
 *  Функция связывания блоков.
 *  Позволяет связать блоки blockFrom и blockTo.
 *  Параметры: strong - создать ли устойчивую связь, clear - очищать ли предыдущие связи.
 **/
fun connectBlocks(blockFrom: Block, blockTo: Block, strong: Boolean = true, clear: Boolean = true) {
    if (blockFrom == blockTo) return
    if (clear) {
        blockFrom.getNextBlock()?.setPrevBlock(null)
        blockTo.getPrevBlock()?.setNextBlock(null)
    }
    blockFrom.setNextBlock(blockTo)
    blockTo.setPrevBlock(blockFrom)
}

/**
 *  Функция развязывания блоков.
 *  Позволяет развязать блоки blockFrom и blockTo.
 **/
fun disconnectBlocks(blockFrom: Block, blockTo: Block) {
    if (blockFrom == blockTo) return
    blockFrom.setNextBlock(null)
    blockTo.setPrevBlock(null)
}