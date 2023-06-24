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

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.varshakulkarni.pedometer.data.repository.PersistentStepsRepository
import dev.varshakulkarni.pedometer.data.sensor.SensorListener
import dev.varshakulkarni.pedometer.utils.NotificationHelper
import dev.varshakulkarni.pedometer.utils.NotificationHelper.Companion.NOTIFICATION_ID
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StepsService : LifecycleService() {

    @Inject
    lateinit var persistentStepsRepository: PersistentStepsRepository

    @Inject
    lateinit var sensorListener: SensorListener

    @Inject
    lateinit var notificationHelper: NotificationHelper


    private var started = false

    override fun onCreate() {
        super.onCreate()
        showNotification()
        startUpdates()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        startUpdates()

        return START_STICKY
    }

    private fun startUpdates() {
        if (!started) {
            lifecycleScope.launch {
                if (ContextCompat.checkSelfPermission(
                        this@StepsService,
                        permission.ACTIVITY_RECOGNITION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    started = true

                    sensorListener.start()
                    sensorListener.sensorSteps.collect(::observeSensorSteps)
                }
            }
            lifecycleScope.launch {
                persistentStepsRepository.steps.collect(::updateNotification)
            }
        }
    }

    private fun updateNotification(steps: Int) {
        if (steps != -1)
            notificationHelper.updateNotification(this, steps)
    }

    private fun observeSensorSteps(sensorSteps: Int) {
        lifecycleScope.launch {
            if (sensorSteps != -1)
                persistentStepsRepository.calculateSteps(sensorSteps)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)

        return null
    }


    private fun showNotification() {
        notificationHelper.createNotificationChannel(this)
        startForeground(NOTIFICATION_ID, notificationHelper.buildNotification(this, 0))
    }
}
