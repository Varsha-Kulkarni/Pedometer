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
package dev.varshakulkarni.pedometer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.varshakulkarni.pedometer.presentation.ui.screens.ChartsScreen
import dev.varshakulkarni.pedometer.presentation.ui.screens.PedometerScreen
import dev.varshakulkarni.pedometer.presentation.ui.screens.SettingsScreen

@Composable
fun PedometerNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = PedometerScreens.PedometerScreen.name
    ) {

        composable(PedometerScreens.PedometerScreen.name) {
            PedometerScreen(
                viewModel = hiltViewModel(),
                onNavigateToCharts = { navController.navigate(PedometerScreens.ChartsScreen.name) },
                onNavToSetTarget = { navController.navigate(PedometerScreens.SettingScreen.name) }
            )
        }

        composable(PedometerScreens.ChartsScreen.name) {
            ChartsScreen (viewModel = hiltViewModel()) { navController.navigateUp() }
        }

        composable(PedometerScreens.SettingScreen.name) {
            SettingsScreen (viewModel = hiltViewModel()){ navController.navigateUp() }
        }
    }
}
