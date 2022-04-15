package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository

class MainViewModel(application: Application):ViewModel() {

    private val database=getDatabase(application)
     private val repository=AsteroidRepository(database)

    val asteroidList=repository.asteroids
}