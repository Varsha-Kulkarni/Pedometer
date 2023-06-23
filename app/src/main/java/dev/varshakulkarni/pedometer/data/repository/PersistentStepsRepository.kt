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

import dev.varshakulkarni.pedometer.data.persistence.dao.StepDao
import dev.varshakulkarni.pedometer.data.persistence.entity.StepEntity
import kotlinx.coroutines.flow.MutableStateFlow
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
) {
    private val _steps = MutableStateFlow(0)
    val steps = _steps.asStateFlow()

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

        if (previousData != null) {
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

                        if (currentDate.hour == previousDate.hour) {
                            val count = previousData.totalStepCount + diff
                            totalSteps = count
                            updateStepData(
                                StepEntity(
                                    "uid",
                                    totalSteps,
                                    unixTime,
                                    sensorCount, diff + previousData.diff, previousData.id
                                )
                            )
                        } else {
                            insertSteps(
                                StepEntity(
                                    uid = "uid",
                                    totalStepCount = totalSteps,
                                    timestamp = unixTime,
                                    sensorStepCount = sensorCount,
                                    diff = diff
                                )
                            )
                        }
                    }
                } else {
                    totalSteps = previousData.totalStepCount
                    updateStepData(
                        StepEntity(
                            "uid",
                            totalSteps,
                            unixTime,
                            previousData.sensorStepCount, 0,
                            previousData.id
                        )
                    )
                }
            } else {
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
            insertSteps(StepEntity("uid", totalSteps, unixTime, sensorCount, 0))
        }
        _steps.value = totalSteps
    }

    private suspend fun updateStepData(stepEntity: StepEntity) = stepsDao.updateStepData(stepEntity)
}
