package com.apochromat.codeblockmobile.logic

/**
 *  Функция связывания блоков.
 *  Позволяет связать блоки blockFrom и blockTo.
 *  Параметры: clear - очищать ли предыдущие связи.
 **/
fun connectBlocks(blockFrom: Block, blockTo: Block, clear: Boolean = true) {
    // Не соединяем блок сам с собой
    if (blockFrom == blockTo) return
    // Если нужно, при соединении очищаем другие связи
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
    blockFrom.nextBlock = null
    blockTo.prevBlock = null
}