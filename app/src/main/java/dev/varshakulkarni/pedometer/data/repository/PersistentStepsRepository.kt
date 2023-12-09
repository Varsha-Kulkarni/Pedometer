/*
 *  Copyright 2023 Varsha Kulkarni
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package dev.varshakulkarni.pedometer.data.repository

import android.util.Log
import dev.varshakulkarni.pedometer.data.persistence.dao.StepDao
import dev.varshakulkarni.pedometer.data.persistence.entity.StepEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersistentStepsRepository @Inject constructor(
    private val stepsDao: StepDao
)  {

    val _steps = MutableStateFlow(0)
    val steps: StateFlow<Int> = _steps.asStateFlow()

    private suspend fun insertSteps(stepEntity: StepEntity) =
        stepsDao.insertSteps(stepEntity)

    private fun getPreviousStepsForUser(uid: String) =
        stepsDao.getPreviousStepsForUser(uid)
            .map {
                it?.let {
                    StepEntity(
                        it.uid,
                        it.totalStepCount,
                        it.timestamp,
                        it.sensorStepCount,
                        it.diff,
                        it.id
                    )
                }
            }

    fun getStepsData() = stepsDao.getStepsForUser("uid")

    suspend fun calculateSteps(sensorCount: Int) {

        val unixTime = System.currentTimeMillis()
        var totalSteps = 0
        val previousData: StepEntity? =
            getPreviousStepsForUser("uid").firstOrNull()

        Log.d("PersistentStepsRepository", "calculateSteps $previousData $sensorCount")
        if (previousData != null) {
            totalSteps = previousData.totalStepCount

            val currentDate = LocalDateTime.now()
            val previousDate = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(previousData.timestamp),
                ZoneId.systemDefault()
            )

            if (currentDate.dayOfMonth == previousDate.dayOfMonth && currentDate.month == previousDate.month) {
                if (sensorCount != 0) {
                    if (sensorCount != previousData.sensorStepCount) {
                        totalSteps = if (sensorCount > previousData.sensorStepCount) {
                            sensorCount - previousData.sensorStepCount + previousData.totalStepCount
                        } else {
                            sensorCount + previousData.totalStepCount
                        }
                        val diff = (totalSteps - previousData.totalStepCount)
                        updateStepData(
                            StepEntity(
                                "uid",
                                totalSteps,
                                unixTime,
                                sensorCount, diff + previousData.diff, previousData.id
                            )
                        )
                    }
                }
            } else {
                val n = currentDate.dayOfMonth - previousDate.dayOfMonth
                if (n > 1) {
                    var i = 1L
                    while (n > i) {
                        insertSteps(
                            StepEntity(
                                uid = "uid",
                                totalStepCount = previousData.totalStepCount,
                                timestamp = previousDate.plusDays(i)
                                    .atZone(ZoneId.systemDefault()).toEpochSecond() * 1000L,
                                sensorStepCount = previousData.sensorStepCount,
                                diff = 0
                            )
                        )
                        i++
                    }
                }
                totalSteps = 0
                insertSteps(
                    StepEntity(
                        "uid",
                        totalSteps,
                        unixTime,
                        previousData.sensorStepCount,
                        0
                    )
                )
            }
        } else {
            var i = 7
            while (0 <= i) {
                insertSteps(
                    StepEntity(
                        "uid",
                        totalSteps,
                        unixTime - (3600000 * 24 * i),
                        sensorCount,
                        0
                    )
                )
                i--
            }
        }
        _steps.value = totalSteps
    }

    private suspend fun updateStepData(stepEntity: StepEntity) = stepsDao.updateStepData(stepEntity)
}

