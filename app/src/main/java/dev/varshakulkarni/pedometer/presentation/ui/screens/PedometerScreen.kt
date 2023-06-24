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
package dev.varshakulkarni.pedometer.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import dev.varshakulkarni.pedometer.R
import dev.varshakulkarni.pedometer.presentation.viewmodels.PedometerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedometerScreen(
    viewModel: PedometerViewModel,
    onNavigateToCharts: () -> Unit,
    onNavToSetTarget: () -> Unit
) {

    val steps = viewModel.state.collectAsState()
    val message =
        if (steps.value.steps > 0) {
            stringResource(id = R.string.steps_count, steps.value.steps)
        } else {
            stringResource(id = R.string.no_record)
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                actions = {
                    IconButton(onClick = onNavigateToCharts) {
                        Icon(
                            painterResource(id = R.drawable.ic_barchart),
                            stringResource(id = R.string.charts_nav)
                        )
                    }
                    IconButton(onClick = onNavToSetTarget) {
                        Icon(painterResource(id = R.drawable.ic_settings),
                         stringResource(id = R.string.set_target))
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}
