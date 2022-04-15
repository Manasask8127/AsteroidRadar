package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid

class AsteroidRepository(private val asteroidDatabase: AsteroidDatabase) {
    val asteroids:LiveData<List<Asteroid>> =
        Transformations.map(asteroidDatabase.asteroidDao.getAsteroids()){
            it.asDomainModel()
        }

    fun refreshAsteroids(){

    }
}