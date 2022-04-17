package com.udacity.asteroidradar.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NeoWs
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.getSeventhDay
import com.udacity.asteroidradar.getToday
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import timber.log.Timber
import java.lang.Exception

class AsteroidRepository(private val asteroidDatabase: AsteroidDatabase) {
//    val asteroids: LiveData<List<Asteroid>> =
//        Transformations.map(asteroidDatabase.asteroidDao.getAsteroids()) {
//            it.asDomainModel()
//        }

    //error while loading need to resolve
    suspend fun refreshAsteroids() {
        var asteroids: ArrayList<Asteroid>
        withContext(Dispatchers.IO) {
            val asteroidsBody: ResponseBody = NeoWs.retrofitService.getAsteroids()
            asteroids = parseAsteroidsJsonResult(JSONObject(asteroidsBody.string()))
            asteroidDatabase.asteroidDao.insertAll(*asteroids.asDatabaseModel())

        }
    }

    suspend fun refreshPicture(): PictureOfDay? {
        var pictureOfDay: PictureOfDay? = null
        withContext(Dispatchers.IO) {
            pictureOfDay = NeoWs.retrofitService.getPhotoOfDay()
        }
        //Timber.d("picture url ${pictureOfDay}")
        return pictureOfDay

    }


    suspend fun  getAsteroidsByWeek():List<Asteroid>{
        var asteroids: List<DatabaseAsteroid>? = null
        withContext(Dispatchers.IO){
            asteroids=asteroidDatabase.asteroidDao.getAsteroidByClosingDate(getToday(),
                getSeventhDay())
        }
        return asteroids!!.asDomainModel()
    }


    suspend fun  getAsteroidsByToday():List<Asteroid>{
        var asteroids: List<DatabaseAsteroid>? = null
        withContext(Dispatchers.IO){
            asteroids=asteroidDatabase.asteroidDao.getAsteroidByClosingDate(getToday(),
                getToday())
        }
        return asteroids!!.asDomainModel()
    }


    suspend fun  getAsteroidsBySave():List<Asteroid>{
        var asteroids: List<DatabaseAsteroid>? = null
        withContext(Dispatchers.IO){
            asteroids=asteroidDatabase.asteroidDao.getAsteroids()
        }
        return asteroids!!.asDomainModel()
    }
}