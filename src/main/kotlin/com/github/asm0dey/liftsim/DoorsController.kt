package com.github.asm0dey.liftsim

import java.math.BigDecimal

object DoorsController {
    @Volatile var doorsClosed = true
        private set
    @Volatile private var openCloseProgress = executors.submit { }

    fun cycleDoorsIfClosed(openCloseTime: BigDecimal) {
        if (!doorsClosed) return
        openCloseProgress = executors.submit {
            doorsClosed = false
            println("Doors are open")
            try {
                Thread.sleep((openCloseTime * BigDecimal("1000")).toLong())
                closeDoorsInternal()
            } catch (ignored: InterruptedException) {
                // we interrupt thread ourselves, so it's ok
            }
        }

    }

    fun closeDoors() {
        openCloseProgress.cancel(true)
        closeDoorsInternal()
    }

    private fun closeDoorsInternal() {
        doorsClosed = true
        println("Doors are closed")
    }


}