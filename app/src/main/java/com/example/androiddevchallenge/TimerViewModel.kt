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

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.concurrent.TimeUnit

class TimerViewModel : ViewModel() {

    private val _time = MutableLiveData("15:00")
    val time: LiveData<String> = _time

    private val _progress = MutableLiveData(0f)
    val progress: LiveData<Float> = _progress

    private val _isRunning = MutableLiveData(false)
    val isRunning: LiveData<Boolean> = _isRunning

    private val targetTime = 15 * 60 * 1000L // 15 minutes
    private val interval = 1 * 1000L // 1 second
    private var currentTimeInMillis: Long? = null
    private var timer: CountDownTimer? = null

    fun onStartClicked() {
        if (isRunning.value == true) {
            timer?.cancel()
            _isRunning.value = false
        } else {
            createTimer()
            timer?.start()
            _isRunning.value = true
        }
    }

    fun onStopClicked() {
        timer?.cancel()
        currentTimeInMillis = null
        _progress.value = 0f
        _time.value = "15:00"
        _isRunning.value = false
    }

    private fun createTimer() {
        timer = object : CountDownTimer(currentTimeInMillis ?: targetTime, interval) {
            override fun onTick(millisUntilFinished: Long) {
                currentTimeInMillis = millisUntilFinished
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                val seconds =
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(
                                millisUntilFinished
                            )
                        )

                val minutesFormatted =
                    if (minutes.toString().length == 1) "0$minutes" else minutes.toString()
                val secondsFormatted =
                    if (seconds.toString().length == 1) "0$seconds" else seconds.toString()

                _progress.value = 1 - (millisUntilFinished.toFloat() / targetTime.toFloat())
                _time.value = "$minutesFormatted:$secondsFormatted"
            }

            override fun onFinish() {
                _progress.value = 1f
                _isRunning.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
        _isRunning.value = false
        currentTimeInMillis = null
    }
}
