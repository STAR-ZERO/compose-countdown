package com.example.androiddevchallenge

import android.os.CountDownTimer
import kotlin.math.ceil

class MyTimer(
    private val onTick: (Int, Int, Float) -> Unit,
    private val onFinish: () -> Unit
) {
    private var timer: CountDownTimer? = null

    private var totalTime: Long = 0

    private var left: Long = 0

    fun start(count: Long) {
        totalTime = count
        startTimer(count)
        timer?.start()
    }

    fun pause() {
        timer?.cancel()
        timer = null
    }

    fun resume() {
        startTimer(left)
    }

    fun reset() {
        pause()
        left = 0
    }

    private fun startTimer(count: Long) {
        timer = object : CountDownTimer(count, 1) {
            override fun onTick(millisUntilFinished: Long) {
                val tickSeconds = ceil(millisUntilFinished / 1000.0).toInt()
                val minutes = tickSeconds / 60
                val seconds = tickSeconds - (minutes * 60)
                val progress = millisUntilFinished.toFloat() / totalTime.toFloat()
                onTick.invoke(minutes, seconds, progress)
                left = millisUntilFinished
            }

            override fun onFinish() {
                onFinish.invoke()
            }

        }.apply {
            start()
        }
    }
}