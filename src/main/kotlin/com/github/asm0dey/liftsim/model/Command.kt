package com.github.asm0dey.liftsim.model

data class Command(val where: Where, val floorNumber: Int)
enum class Where { INSIDE, OUTSIDE }
