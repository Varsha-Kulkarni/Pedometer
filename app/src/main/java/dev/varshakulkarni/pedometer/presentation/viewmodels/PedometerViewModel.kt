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
import dev.varshakulkarni.pedometer.data.repository.PersistentStepsRepository
import dev.varshakulkarni.pedometer.presentation.states.PedometerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PedometerViewModel @Inject constructor(
    private val persistentStepsRepository: PersistentStepsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(PedometerState())
    val state: StateFlow<PedometerState> = _state.asStateFlow()

    init {
        observeSteps()
    }

    private fun observeSteps() {
        viewModelScope.launch {
            persistentStepsRepository.steps.collect {
                _state.update { state ->
                    state.copy(
                        steps = it
                    )
                }
            }
        }
    }
}
