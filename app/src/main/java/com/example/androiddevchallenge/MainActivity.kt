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
package com.example.androiddevchallenge

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieAnimationSpec
import com.airbnb.lottie.compose.rememberLottieAnimationState
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {
    private val timerViewModel by viewModels<TimerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContent {
            MyTheme {
                MyApp(timerViewModel)
            }
        }
    }
}

@Composable
fun MyApp(viewModel: TimerViewModel) {
    val time by viewModel.time.observeAsState("15:00")
    val progress by viewModel.progress.observeAsState(0f)
    val isRunning by viewModel.isRunning.observeAsState(false)

    Box {
        Image(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            painter = painterResource(id = R.drawable.ic_background),
            contentDescription = "background"
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimeCircleView(time = time, progress = progress)
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                FloatingActionButton(
                    shape = CircleShape,
                    backgroundColor = Color(231, 216, 252, 255),
                    onClick = { viewModel.onStartClicked() }
                ) {
                    val icon = if (isRunning) R.drawable.ic_pause else R.drawable.ic_start
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = "toggle"
                    )
                }
                FloatingActionButton(
                    shape = CircleShape,
                    backgroundColor = Color(231, 216, 252, 255),
                    onClick = { viewModel.onStopClicked() }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_stop),
                        contentDescription = "toggle"
                    )
                }
            }
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            val animationSpec = remember { LottieAnimationSpec.RawRes(R.raw.animation) }
            val animationState =
                rememberLottieAnimationState(autoPlay = true, repeatCount = Integer.MAX_VALUE)

            LottieAnimation(
                spec = animationSpec,
                modifier = Modifier.fillMaxSize(),
                animationState = animationState
            )
        }
    }
}

@Composable
fun TimeCircleView(time: String, progress: Float) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.padding(top = 64.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(200.dp),
            progress = progress,
            color = Color(246, 211, 145),
            strokeWidth = 8.dp
        )
        Box(contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
                    .clip(CircleShape)
            ) {
                Box(
                    modifier = Modifier
                        .requiredSize(160.dp)
                        .background(Color(231, 216, 252, 255))
                )
            }
            Text(
                text = time,
                color = Color(11, 45, 77),
                style = TextStyle(fontSize = 27.sp)
            )
        }
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp(TimerViewModel())
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp(TimerViewModel())
    }
}
