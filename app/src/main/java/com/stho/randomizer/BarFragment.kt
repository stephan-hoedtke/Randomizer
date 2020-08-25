package com.stho.randomizer

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.bar_fragment.view.*
import java.text.DecimalFormat


class BarFragment : Fragment() {

    private lateinit var viewModel: BarViewModel
    private val handler = Handler()
    private var isRunning: Boolean = false
    private var previousPi: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bar_fragment, container, false)
        viewModel.barsDataLD.observe(viewLifecycleOwner, { c -> updateBars(c) })
        viewModel.piLD.observe(viewLifecycleOwner, { pi -> updatePi(pi) })
        view?.buttonPauseResume?.setOnClickListener { onPauseResume() }
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BarViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        handler.post(runnable)
        isRunning = true
    }

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            viewModel.updateData()
            handler.postDelayed(this, 10)
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
        isRunning = false
    }

    private fun updateBars(barData: BarData) {
        view?.apply {
            val b = barData.Bars
            val t = barData.TotalCounter
            if (t > 0) {
                val f = 50.0 * 10 / t
                this.b1.progress = (f * b[0]).toInt()
                this.b2.progress = (f * b[1]).toInt()
                this.b3.progress = (f * b[2]).toInt()
                this.b4.progress = (f * b[3]).toInt()
                this.b5.progress = (f * b[4]).toInt()
                this.b6.progress = (f * b[5]).toInt()
                this.b7.progress = (f * b[6]).toInt()
                this.b8.progress = (f * b[7]).toInt()
                this.b9.progress = (f * b[8]).toInt()
                this.b10.progress = (f * b[9]).toInt()
            }
            this.totalCounter.text = barData.TotalCounter.toString()
        }
    }

    private fun updatePi(pi: Double) {
        view?.apply {
            val newPi = pi.format(10)
            if (newPi.compareTo(previousPi) != 0) {
                var digits = 0
                while (digits < newPi.length && digits < previousPi.length && newPi[digits] == previousPi[digits]) {
                    digits++
                }
                previousPi = newPi
                this.pi.text = getColoredString(newPi, Color.RED, digits)
            }
        }
    }

    private fun onPauseResume() {

        if (isRunning) {
            isRunning = false
            handler.removeCallbacks(runnable)
            view?.buttonPauseResume?.setImageResource(android.R.drawable.ic_media_play)
        }
        else {
            isRunning = true
            handler.post(runnable)
            view?.buttonPauseResume?.setImageResource(android.R.drawable.ic_media_pause)
        }
    }

    companion object {
        private fun Double.format(fractionDigits: Int): String {
            val df = DecimalFormat()
            df.maximumFractionDigits = fractionDigits
            df.minimumFractionDigits = fractionDigits
            return df.format(this)
        }
    }

    fun getColoredString(string: String, color: Int, digits: Int): SpannableString? {
        val spannableString = SpannableString(string)
        spannableString.setSpan(ForegroundColorSpan(color),0, digits, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        return spannableString
    }
}