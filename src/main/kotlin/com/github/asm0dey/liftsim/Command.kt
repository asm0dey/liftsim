package com.github.asm0dey.liftsim

data class Command(val where: Where, val floorNumber: Int)
enum class Where { INSIDE, OUTSIDE }
