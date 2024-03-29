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
package dev.varshakulkarni.pedometer.presentation.ui

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import dagger.hilt.android.AndroidEntryPoint
import dev.varshakulkarni.pedometer.R
import dev.varshakulkarni.pedometer.StepsService
import dev.varshakulkarni.pedometer.presentation.navigation.PedometerNavigation
import dev.varshakulkarni.pedometer.presentation.ui.components.PedometerDialog
import dev.varshakulkarni.pedometer.presentation.ui.theme.PedometerTheme
import dev.varshakulkarni.pedometer.utils.openAppSettings

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PedometerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Permission()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun Permission() {
        var showDialog by remember { mutableStateOf(true) }

        val permissionState =
            rememberPermissionState(permission = Manifest.permission.ACTIVITY_RECOGNITION)

        var permissionAlreadyRequested by rememberSaveable {
            mutableStateOf(false)
        }

        when {
            permissionState.status.isGranted -> {
                startStepsService()
                PedometerNavigation()
            }

            !permissionAlreadyRequested && !permissionState.status.shouldShowRationale -> {
                permissionAlreadyRequested = true

                SideEffect {
                    permissionState.launchPermissionRequest()
                }
            }

            permissionState.status.shouldShowRationale -> {
                if (showDialog) {
                    PedometerDialog(
                        dialogTitle = R.string.permission_rationale_dialog_title,
                        dialogText = R.string.permission_rationale_dialog_message,
                        onDismiss = { showDialog = false },
                        onOkay = {
                            showDialog = false
                            permissionState.launchPermissionRequest()
                        },
                    )
                }
            }

            else -> {
                PedometerDialog(
                    dialogTitle = R.string.permission_rationale_dialog_title,
                    dialogText = R.string.permission_fully_denied,
                    onDismiss = { showDialog = false },
                    onOkay = { openAppSettings() }
                )
            }
        }
    }

    private fun startStepsService() {
        val serviceIntent = Intent(this, StepsService::class.java)
        startForegroundService(serviceIntent)
    }
}
