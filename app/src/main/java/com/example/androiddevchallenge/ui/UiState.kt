package com.example.androiddevchallenge.ui

data class UiState(
    val timerState: TimerState = TimerState.READY,
    val minutes: Int = 0,
    val seconds: Int = 0,
    val progress: Float = 0f,
)

enum class TimerState {
    READY,
    RUNNING,
    PAUSE,
}
