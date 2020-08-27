package com.stho.randomizer

import androidx.lifecycle.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random

class BarViewModel : ViewModel() {

    private val dimensionLiveData: MutableLiveData<Int> = MutableLiveData<Int>()
    private val sizeLiveData: MutableLiveData<Int> = MutableLiveData<Int>()
    private val barsLiveData: MutableLiveData<BarData> = MutableLiveData<BarData>()

    internal val barsDataLD: LiveData<BarData>
        get() = barsLiveData

    internal val piLD: LiveData<Double>
        get() = Transformations.map(barsLiveData) { x -> x.pi }

    init {
        dimensionLiveData.value = DEFAULT_DIMENSION
        sizeLiveData.value = DEFAULT_SIZE
        barsLiveData.value = BarData()
    }

    internal fun updateData() {
        val dimension: Int = dimensionLiveData.value ?: DEFAULT_DIMENSION
        val size: Int = sizeLiveData.value ?: DEFAULT_SIZE
        updateData(dimension, size)
     }

    private fun updateData(dimension: Int, size: Int) {

        val min: Double = -1.0
        val max: Double = +1.0

        val bars: BarData = barsLiveData.value ?: BarData()

        val random= ThreadLocalRandom.current()
        val xValues = List(size) { random.nextDouble(min, max) }
        val yValues = List(size) { random.nextDouble(min, max) }
        val delta = (max - min) / dimension
        for (i in 0 until size) {

            val x = xValues[i]
            val y = yValues[i]

            val ix: Int = ((x - min) / delta).toInt()
            bars.count(ix)

            val iy: Int = ((y - min) / delta).toInt()
            bars.count(iy)

            val f = x * x + y * y
            if (f <= 1.0)
                bars.countInsideCircle()
            else
                bars.countOutsideCircle()

        }

        barsLiveData.postValue(bars)
    }

    private val random: java.util.Random
        get() = ThreadLocalRandom.current()

    companion object {
        private const val DEFAULT_SIZE: Int = 100000
        private const val DEFAULT_DIMENSION: Int = 10
    }
}