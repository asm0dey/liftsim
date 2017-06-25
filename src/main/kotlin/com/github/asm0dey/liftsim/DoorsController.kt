package com.github.asm0dey.liftsim

import java.math.BigDecimal
import kotlin.concurrent.thread

object DoorsController {
    @Volatile private var doorsClosed = true
    @Volatile private var openCloseProgress: Thread = thread(start = false) { }

    fun cycleDoorsIfClosed(openCloseTime: BigDecimal) {
        openCloseProgress = thread(name = "doors-controller") {
            if (!doorsClosed) return@thread
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
        if (openCloseProgress.isAlive)
            openCloseProgress.interrupt()
        closeDoorsInternal()
    }

    private fun closeDoorsInternal() {
        doorsClosed = true
        println("Doors are closed")
    }

    fun doorsOpened() = !doorsClosed
}