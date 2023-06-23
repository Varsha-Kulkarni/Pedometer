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
package dev.varshakulkarni.pedometer.data.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.varshakulkarni.pedometer.data.persistence.entity.StepEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StepDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSteps(stepEntity: StepEntity)

    @Query("SELECT * FROM Step WHERE uid=:uid")
    fun getStepsForUser(uid: String): Flow<List<StepEntity>>

    @Query("SELECT * FROM Step WHERE id=(SELECT max(id) FROM Step) AND uid = :uid")
    fun getPreviousStepsForUser(uid: String): Flow<StepEntity?>

    @Update
    suspend fun updateStepData(stepEntity: StepEntity)
}
