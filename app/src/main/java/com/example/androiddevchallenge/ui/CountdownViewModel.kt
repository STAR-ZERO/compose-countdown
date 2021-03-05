package com.example.androiddevchallenge.ui

import androidx.lifecycle.ViewModel
import com.example.androiddevchallenge.MyTimer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CountdownViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val timer = MyTimer(
        onTick = { minutes, seconds, progress ->
            _uiState.value = _uiState.value.run {
                copy(
                    minutes = minutes,
                    seconds = seconds,
                    progress = progress,
                )
            }
        },
        onFinish = {
            _uiState.value = _uiState.value.run {
                copy(
                    timerState = TimerState.READY,
                    minutes = 0,
                    seconds = 0,
                    progress = 0f,
                )
            }
        }
    )

    fun incrementMinutes() {
        if (_uiState.value.minutes >= 99) {
            return
        }
        _uiState.value = _uiState.value.run {
            copy(minutes = minutes + 1)
        }
    }

    fun decrementMinutes() {
        if (_uiState.value.minutes <= 0) {
            return
        }
        _uiState.value = _uiState.value.run {
            copy(minutes = minutes - 1)
        }
    }

    fun incrementSeconds() {
        if (_uiState.value.seconds >= 59) {
            return
        }
        _uiState.value = _uiState.value.run {
            copy(seconds = seconds + 1)
        }
    }

    fun decrementSeconds() {
        if (_uiState.value.seconds <= 0) {
            return
        }
        _uiState.value = _uiState.value.run {
            copy(seconds = seconds - 1)
        }
    }

    fun startTimer() {
        val millis = _uiState.value.run {
            ((minutes * 60 + seconds) * 1000).toLong()
        }

        _uiState.value = _uiState.value.run {
            copy(timerState = TimerState.RUNNING)
        }

        timer.start(millis)
    }

    fun pauseTimer() {
        timer.pause()
        _uiState.value = _uiState.value.run {
            copy(timerState = TimerState.PAUSE)
        }
    }

    fun resumeTimer() {
        timer.resume()
        _uiState.value = _uiState.value.run {
            copy(timerState = TimerState.RUNNING)
        }
    }

    fun resetTimer() {
        timer.reset()
        _uiState.value = _uiState.value.run {
            copy(
                timerState = TimerState.READY,
                minutes = 0,
                seconds = 0,
                progress = 0f,
            )
        }
    }
}