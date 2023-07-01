/*
 * Copyright 2023 Varsha Kulkarni
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
package dev.varshakulkarni.pedometer.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import dev.varshakulkarni.pedometer.data.repository.PersistentStepsRepository
import javax.inject.Inject

@AndroidEntryPoint
class ResetCountBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var localStepsRepository: PersistentStepsRepository

    @Inject
    lateinit var utils: Utils

    override fun onReceive(context: Context, intent: Intent) = goAsync {
        Log.d("", "Reset Count")
        localStepsRepository.calculateSteps(0)
        utils.setAlarmToResetStepCount(context)
    }
}
