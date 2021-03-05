package com.example.androiddevchallenge.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CountdownScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Countdown Timer") }
            )
        }
    ) {
        Content()
    }
}

@Composable
fun Content() {
    val viewModel = viewModel<CountdownViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    Box {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Counter(
                    uiState.minutes,
                    uiState.timerState,
                    "m",
                    { viewModel.incrementMinutes() },
                    { viewModel.decrementMinutes() }
                )

                Counter(
                    uiState.seconds,
                    uiState.timerState,
                    "s",
                    { viewModel.incrementSeconds() },
                    { viewModel.decrementSeconds() }
                )
            }

        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.Bottom
            ) {
                TimerButtons(
                    timerState = uiState.timerState,
                    startTimer = { viewModel.startTimer() },
                    pauseTimer = { viewModel.pauseTimer() },
                    resumeTimer = { viewModel.resumeTimer() },
                    resetTimer = { viewModel.resetTimer() }
                )
            }
        }
    }
}

@Composable
fun Counter(
    value: Int,
    timerState: TimerState,
    unit: String,
    increment: () -> Unit,
    decrement: () -> Unit
) {
    val formattedValue = remember(value) { String.format("%02d", value) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (timerState == TimerState.READY) {
            Button(
                onClick = { increment.invoke() },
            ) {
                Text(text = "+")
            }
        }

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = formattedValue,
                style = MaterialTheme.typography.h3,
                textAlign = TextAlign.End
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = unit,
                style = MaterialTheme.typography.h5,
            )
        }

        if (timerState == TimerState.READY) {
            Button(
                onClick = { decrement.invoke() },
            ) {
                Text(text = "-")
            }
        }
    }
}

@Composable
fun TimerButtons(
    timerState: TimerState,
    startTimer: () -> Unit,
    pauseTimer: () -> Unit,
    resumeTimer: () -> Unit,
    resetTimer: () -> Unit
) {

    Column {
        when (timerState) {
            TimerState.READY -> {
                Button(onClick = {
                    startTimer.invoke()
                }) {
                    Text(text = "START")
                }
            }
            TimerState.RUNNING -> {
                Button(onClick = {
                    pauseTimer.invoke()
                }) {
                    Text(text = "PAUSE")
                }
            }
            TimerState.PAUSE -> {
                Row {
                    Button(onClick = {
                        resetTimer.invoke()
                    }) {
                        Text(text = "RESET")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(onClick = {
                        resumeTimer.invoke()
                    }) {
                        Text(text = "RESUME")
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}