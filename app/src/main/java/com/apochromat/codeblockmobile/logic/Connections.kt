package com.apochromat.codeblockmobile.logic

/**
 *  Функция связывания блоков.
 *  Позволяет связать блоки blockFrom и blockTo.
 *  Параметры: strong - создать ли устойчивую связь, clear - очищать ли предыдущие связи.
 **/
fun connectBlocks(blockFrom: Block, blockTo: Block, strong: Boolean = true, clear: Boolean = true) {
    if (blockFrom == blockTo) return
    if (clear) {
        blockFrom.nextBlock?.prevBlock = null
        blockTo.prevBlock?.nextBlock = null
    }
    blockFrom.nextBlock = blockTo
    blockTo.prevBlock = blockFrom
}

/**
 *  Функция развязывания блоков.
 *  Позволяет развязать блоки blockFrom и blockTo.
 **/
fun disconnectBlocks(blockFrom: Block, blockTo: Block) {
    if (blockFrom == blockTo) return
    blockFrom.nextBlock = null
    blockTo.prevBlock = null
}