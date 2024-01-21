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
package dev.varshakulkarni.pedometer.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.varshakulkarni.pedometer.R
import dev.varshakulkarni.pedometer.presentation.viewmodels.ChartViewModel
import dev.varshakulkarni.scrollablebarchart.BarChartDataCollection
import dev.varshakulkarni.scrollablebarchart.BarChartDefaults
import dev.varshakulkarni.scrollablebarchart.SPACING_MEDIUM
import dev.varshakulkarni.scrollablebarchart.ui.chart.RTLScrollableBarChart

@Composable
fun ChartsScreen(viewModel: ChartViewModel, onNavigationUp: () -> Unit) {
    val state by viewModel.state.collectAsState()

    if (state.bars.isNotEmpty()) {
        ChartsContent(
            stepDataCollection = BarChartDataCollection(state.bars),
            target = state.target,
            modifier = Modifier,
            onNavigationUp = onNavigationUp
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ChartsContent(
    stepDataCollection: BarChartDataCollection,
    target: Int,
    modifier: Modifier,
    onNavigationUp: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                navigationIcon = {
                    IconButton(
                        modifier = Modifier.padding(start = 4.dp),
                        onClick = onNavigationUp
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_back),
                            "Back",
                        )
                    }
                },
            )
        },

        content = {
            Column(modifier = modifier.padding(it)) {
                RTLScrollableBarChart(
                    barChartDataCollection = stepDataCollection,
                    target = target,
                    modifier = modifier.padding(SPACING_MEDIUM.dp),
                    chartSize = BarChartDefaults.chartSize(600.dp, 500.dp)
                )
            }
        }
    )
}
