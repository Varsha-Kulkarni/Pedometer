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
import dev.varshakulkarni.pedometer.presentation.states.SettingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: DataStoreRepository
): ViewModel() {

    private val _state = MutableStateFlow(SettingState())
    val state = _state.asStateFlow()

    init {
        observeTarget()
    }

    private fun observeTarget() {
        viewModelScope.launch {
            _state.update {state->
                state.copy(
                    target = repository.targetFlow.first()
                )
            }
        }

    }

    fun updateTarget(target: Int){
        viewModelScope.launch {
            repository.putInt("StepsTarget", target)
        }
    }
}