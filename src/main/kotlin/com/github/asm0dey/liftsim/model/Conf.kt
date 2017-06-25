package com.github.asm0dey.liftsim.model

import com.beust.jcommander.IValueValidator
import com.beust.jcommander.Parameter
import com.beust.jcommander.ParameterException
import java.math.BigDecimal

/**
 * Created by finkel on 24.06.17.
 */
class BuildingAndLiftConfig {
    @Parameter(
            names = arrayOf("-f", "--floors-num"),
            required = true,
            validateValueWith = arrayOf(FloorNumValidator::class),
            description = "Number of floors in building, where 5<=f<=20 "
    )
    var floors: Int? = null

    @Parameter(
            names = arrayOf("-s", "--speed"),
            required = true,
            validateValueWith = arrayOf(PositinveValidator::class),
            description = "Speed, m/s, should be >0"
    )
    lateinit var speed: BigDecimal

    @Parameter(
            names = arrayOf("-h", "--floor-height"),
            required = true,
            validateValueWith = arrayOf(PositinveValidator::class),
            description = "Floor height, m, should be >0"
    )
    lateinit var floorHeight: BigDecimal

    @Parameter(
            names = arrayOf("-t", "--open-close-time"),
            required = true,
            validateValueWith = arrayOf(PositinveValidator::class),
            description = "Time, elevator needs to close the doors (s), should be >0"
    )
    lateinit var openCloseTime: BigDecimal

}

class FloorNumValidator : IValueValidator<Int> {
    override fun validate(name: String?, value: Int?) =
            if (value == null)
                throw ParameterException("Floor number can't be null")
            else if (value < 5)
                throw ParameterException("There should not be less then 5 floors in building")
            else if (value > 20)
                throw ParameterException("There should not be more then 20 floors in building")
            else {
            }

}

class PositinveValidator : IValueValidator<BigDecimal> {
    override fun validate(name: String?, value: BigDecimal?) =
            if (value != null && value <= BigDecimal.ZERO)
                throw ParameterException("Value should be positive")
            else {
            }
}