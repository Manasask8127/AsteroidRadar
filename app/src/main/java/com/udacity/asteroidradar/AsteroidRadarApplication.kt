package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import com.udacity.asteroidradar.work.WorkOnRefreshAsteroids
import com.udacity.asteroidradar.work.WorkOnDeleteAsteroids
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class AsteroidRadarApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        DoInBackgroud()
    }

    private fun DoInBackgroud(){
        val applicationScope= CoroutineScope(Dispatchers.Default)
        applicationScope.launch {
            val constraints=Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(true)
                .apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        setRequiresDeviceIdle(true)
                    }
                }.build()

            val workOnDeleteAsteroidsRequest= PeriodicWorkRequestBuilder<WorkOnDeleteAsteroids>(
                1,TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()


            WorkManager.getInstance().enqueueUniquePeriodicWork(
                Constants.DELETE_ASTEROIDS_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                workOnDeleteAsteroidsRequest
            )


            val workOnRefreshAsteroidsRequest= PeriodicWorkRequestBuilder<WorkOnRefreshAsteroids>(
                1,TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()


            WorkManager.getInstance().enqueueUniquePeriodicWork(
                Constants.DELETE_ASTEROIDS_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                workOnRefreshAsteroidsRequest
            )
        }
    }
}