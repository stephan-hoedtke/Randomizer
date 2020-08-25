package com.stho.randomizer

class BarData {
    private var bars: Array<Int> = Array<Int>(10) { 0 }
    private var totalCounter: Int = 0
    private var insideCircleCounter: Int = 0
    private var outsideCircleCounter: Int = 0

    internal fun count(index: Int) {
        if (index >= 0 && index <= bars.size) {
            totalCounter += 1
            bars[index] += 1
        }
    }

    internal fun countInsideCircle() {
        insideCircleCounter++
    }

    internal fun countOutsideCircle() {
        outsideCircleCounter++
    }

    internal val Bars: Array<Int>
        get() = bars

    internal val TotalCounter
        get() = totalCounter

    internal val pi: Double
        get() = 4.0 * insideCircleCounter.toDouble() / (insideCircleCounter + outsideCircleCounter).toDouble()
}