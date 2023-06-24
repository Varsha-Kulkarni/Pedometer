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
package dev.varshakulkarni.pedometer.di

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.hardware.SensorManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.varshakulkarni.pedometer.data.repository.DataStoreRepository
import dev.varshakulkarni.pedometer.data.repository.DataStoreRepositoryImpl
import dev.varshakulkarni.pedometer.data.repository.settingsDataStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSensorManager(
        application: Application
    ) = application.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    @Provides
    @Singleton
    fun provideDataStoreRepository(application: Application): DataStoreRepository =
        DataStoreRepositoryImpl(application.settingsDataStore)


    @Provides
    @Singleton
    fun provideNotificationManager(application: Application) =
        application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
}
