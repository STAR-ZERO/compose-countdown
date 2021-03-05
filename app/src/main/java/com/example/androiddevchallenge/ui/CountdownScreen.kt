/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
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

        TimerCircle(uiState.progress)

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
                Icon(Icons.Filled.ExpandLess, contentDescription = "Up")
            }
        }

        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
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
                Icon(Icons.Filled.ExpandMore, contentDescription = "Down")
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
                Button(
                    onClick = {
                        startTimer.invoke()
                    }
                ) {
                    Text(text = "START")
                }
            }
            TimerState.RUNNING -> {
                Button(
                    onClick = {
                        pauseTimer.invoke()
                    }
                ) {
                    Text(text = "PAUSE")
                }
            }
            TimerState.PAUSE -> {
                Row {
                    Button(
                        onClick = {
                            resetTimer.invoke()
                        }
                    ) {
                        Text(text = "RESET")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            resumeTimer.invoke()
                        }
                    ) {
                        Text(text = "RESUME")
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun TimerCircle(progress: Float) {
    val progressColor = MaterialTheme.colors.primary
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        drawCircle(
            color = Color.LightGray,
            center = Offset(x = canvasWidth / 2, y = canvasHeight / 2),
            radius = size.width / 2,
            style = Stroke(8.dp.toPx())
        )

        if (progress > 0) {
            rotate(degrees = -90f) {
                drawArc(
                    color = progressColor,
                    startAngle = 0f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    topLeft = Offset(x = 0f, y = canvasHeight / 2 - canvasWidth / 2),
                    size = Size(canvasWidth, canvasWidth),
                    style = Stroke(8.dp.toPx())
                )
            }
        }
    }
}
