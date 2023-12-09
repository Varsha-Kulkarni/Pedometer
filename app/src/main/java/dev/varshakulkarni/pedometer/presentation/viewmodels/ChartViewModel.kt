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

package dev.varshakulkarni.pedometer.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.varshakulkarni.pedometer.data.repository.DataStoreRepository
import dev.varshakulkarni.pedometer.data.repository.PersistentStepsRepository
import dev.varshakulkarni.pedometer.presentation.states.ChartState
import dev.varshakulkarni.pedometer.utils.Constants
import dev.varshakulkarni.scrollablebarchart.ChartData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val persistentStepsRepository: PersistentStepsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ChartState())
    val state: StateFlow<ChartState> = _state.asStateFlow()

    init {
        observeStepData()
        observeTarget()

    }

    private fun observeTarget() {
        viewModelScope.launch {
            val target = dataStoreRepository.targetFlow.first()
            _state.update { state ->
                state.copy(
                    target = if (target > 0) target else Constants.DEFAULT_TARGET
                )
            }
        }
    }

    private fun observeStepData() {
        persistentStepsRepository.getStepsData()
            .distinctUntilChanged()
            .onEach {
                _state.update { state ->
                    state.copy(
                        bars = it.map { step ->
                            ChartData(
                                Instant.ofEpochMilli(step.timestamp)
                                    .atZone(ZoneId.systemDefault())
                                    .dayOfMonth, step.totalStepCount
                            )
                        }
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}