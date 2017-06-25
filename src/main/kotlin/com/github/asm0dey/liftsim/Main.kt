package com.github.asm0dey.liftsim

import com.beust.jcommander.JCommander
import com.beust.jcommander.ParameterException
import com.github.asm0dey.liftsim.model.BuildingAndLiftConfig
import com.github.asm0dey.liftsim.model.Command
import com.github.asm0dey.liftsim.model.Where.INSIDE
import com.github.asm0dey.liftsim.model.Where.OUTSIDE
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

val executors: ExecutorService = Executors.newFixedThreadPool(3)

fun main(args: Array<String>) {
    val conf = BuildingAndLiftConfig()
    val jCommander = JCommander
            .newBuilder()
            .programName("lift-test")
            .addObject(conf)
            .build()
    if (args.contains("--help")) {
        jCommander.usage()
        System.exit(0)
    }
    if (args.size != 8) {
        jCommander.usage()
        System.exit(1)
    }
    try {
        jCommander.parse(*args)
    } catch (e: ParameterException) {
        e.printStackTrace()
        jCommander.usage()
        System.exit(1)
    }

    println("""
    Building house with ${conf.floors} floors, ${conf.floorHeight} m. floor height
    and elevator with following characteristics:
     speed: ${conf.speed} m/s,
     time from door opening to automatic close ${conf.openCloseTime} s. 😉""")
    Thread.sleep(300)
    println("""
    done.
    Available commands:
      i fn — send elevator to floor fn from inside
      o fn — call elevator to floor fn from outside
      exit — exit application

    See more ad README.md

""")

    LiftController.launch(conf)
    val parser = parser(conf)
    while (true) {
        val readLine = readLine()?.replace("\n", "")?.trim() ?: continue
        if ("exit" == readLine) break
        try {
            LiftController.invoke(parser(readLine))
        } catch (e: IllegalArgumentException) {
            System.err.println(e)
        }
    }
    System.exit(0)
}

private fun parser(conf: BuildingAndLiftConfig): (String) -> Command {
    return {
        val split = it.split(" ")
        if (split.size != 2)
            throw IllegalArgumentException("command is in incorrect format")
        if (!(split[0].equals("i", true) || split[0].equals("o", true)))
            throw IllegalArgumentException("it neither outside nor inside elevator call. Please, check")
        try {
            val floorNumber = split[1].toInt()
            if (floorNumber > conf.floors!! || floorNumber < 1)
                throw IllegalArgumentException("Call from nonexistent floor! It's miracle! But not reacting")
            Command(
                    where = if (split[0].equals("i", true)) INSIDE else OUTSIDE,
                    floorNumber = floorNumber
            )
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Illegal floor number: ${split[1]}", e)
        }
    }
}

