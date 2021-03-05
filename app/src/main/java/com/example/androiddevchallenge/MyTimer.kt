package com.example.androiddevchallenge

import android.os.CountDownTimer
import kotlin.math.ceil

class MyTimer(
    private val onTick: (Int, Int) -> Unit,
    private val onFinish: () -> Unit
) {
    private var timer: CountDownTimer? = null

    private var left: Long = 0

    fun start(count: Long) {
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
        timer = object : CountDownTimer(count, 200) {
            override fun onTick(millisUntilFinished: Long) {
                val tickSeconds = ceil(millisUntilFinished / 1000.0).toInt()
                val minutes = tickSeconds / 60
                val seconds = tickSeconds - (minutes * 60)
                onTick.invoke(minutes, seconds)
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