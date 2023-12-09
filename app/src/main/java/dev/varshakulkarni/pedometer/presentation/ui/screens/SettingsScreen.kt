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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.varshakulkarni.pedometer.R
import dev.varshakulkarni.pedometer.presentation.ui.components.NumberPicker
import dev.varshakulkarni.pedometer.presentation.viewmodels.SettingsViewModel
import dev.varshakulkarni.scrollablebarchart.utils.rememberComposeImmutableList

@Composable
fun SettingsScreen(viewModel: SettingsViewModel, onNavigationUp: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsContent(
        target = state.target,
        onUpdateTarget = { viewModel.updateTarget(it) },
        onNavigationUp = onNavigationUp
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SettingsContent(
    target: Int,
    onUpdateTarget: (Int) -> Unit,
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
                            stringResource(id = R.string.back),
                        )
                    }
                },
            )
        },

        content = { paddingValues ->

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                val values = remember { (1000..15000 step 100).map { it } }
                val immutableList by rememberComposeImmutableList {
                    values
                }

                Text(
                    text = stringResource(id = R.string.daily_target),
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                if (target > 0) {
                    NumberPicker(
                        items = immutableList,
                        visibleItemsCount = 3,
                        startIndex = values.indexOf(target),
                        modifier = Modifier.weight(0.3f),
                        textModifier = Modifier.padding(8.dp),
                        textStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                        onTargetSelected = onUpdateTarget
                    )
                }
            }
        })
}
