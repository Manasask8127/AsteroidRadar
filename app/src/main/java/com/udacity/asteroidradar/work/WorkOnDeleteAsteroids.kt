package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import java.sql.SQLDataException
import java.sql.SQLException

class WorkOnDeleteAsteroids(context: Context,workerParameters: WorkerParameters):
    CoroutineWorker(context,workerParameters) {
    override suspend fun doWork(): Result {
        val database= getDatabase(applicationContext)
        val repository=AsteroidRepository(database)

        return try {
            repository.deletePastAsteroids()
            Result.success()
        }
        catch (exception: SQLException) {
            Result.retry()
        }
    }

}