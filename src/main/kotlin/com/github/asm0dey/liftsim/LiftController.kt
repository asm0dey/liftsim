package com.github.asm0dey.liftsim

import com.github.asm0dey.liftsim.DoorsController.closeDoors
import com.github.asm0dey.liftsim.model.BuildingAndLiftConfig
import java.math.BigDecimal
import java.util.concurrent.LinkedBlockingDeque
import kotlin.concurrent.thread

object LiftController {
    private val commands = LinkedBlockingDeque<Command>()
    @Volatile private var busy = false
    @Volatile private var currentFloor = 1

    fun launch(conf: BuildingAndLiftConfig) = thread(name = "lift-controller") {
        val openClose = { DoorsController.openCloseCycle(conf.openCloseTime) }
        while (true) {
            val (where, targetFloor) = commands.take()!!

            if (targetFloor != currentFloor && DoorsController.doorsOpened() && where != Where.INSIDE || busy) {
                println("Elevator is busy. Please, try again later.")
                continue
            }

            if (targetFloor > conf.floors!! || targetFloor < 1) {
                println("Elevator is called from nonexistent floor. Ignoring")
                continue
            }

            if (currentFloor == targetFloor) {
                openClose()
                continue
            }

            if (where == Where.INSIDE && DoorsController.doorsOpened()) {
                closeDoors()
            }

            busy = true
            //we launch it in separate thread to be able to handle commands as soon as possible,
            // not waiting for lift finish its actions
            thread {
                val timePerFloor = conf.floorHeight / conf.speed
                val range =
                        if (currentFloor < targetFloor) (currentFloor + 1..targetFloor)
                        else (currentFloor - 1 downTo targetFloor)
                range.forEach {
                    Thread.sleep((timePerFloor * BigDecimal("1000")).toLong())
                    println("On floor $it")
                }
                openClose()
                currentFloor = targetFloor
                busy = false
            }
        }
    }

    fun addCommand(c: Command) = commands.add(c)
}