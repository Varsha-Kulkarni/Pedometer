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
package dev.varshakulkarni.pedometer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import dev.varshakulkarni.pedometer.utils.Utils
import javax.inject.Inject

@HiltAndroidApp
class PedometerApp : Application() {
    @Inject
    lateinit var utils: Utils

    override fun onCreate() {
        super.onCreate()

        utils.setAlarmToResetStepCount(this)
    }
}
