package com.udacity.asteroidradar.main

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.checKNetwork
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel(val application: Application):ViewModel() {

    private val database=getDatabase(application)
     private val repository=AsteroidRepository(database)


    private var _navigateToDetails= MutableLiveData<Asteroid>()

    val navigateToDetails:LiveData<Asteroid>
    get() = _navigateToDetails

    private val _pictureOfDay=MutableLiveData<PictureOfDay>()
    val pictureOfDay:LiveData<PictureOfDay>
    get() = _pictureOfDay


//    val asteroidList=repository.asteroids

    private var _asteroidList=MutableLiveData<List<Asteroid>>()
    val asteroidList:LiveData<List<Asteroid>>
    get() = _asteroidList


    fun getAsteroidsByWeek(){
        viewModelScope.launch {
            _asteroidList.value=repository.getAsteroidsByWeek()
        }
    }

    fun getAsteroidsByToday(){
        viewModelScope.launch {
            _asteroidList.value=repository.getAsteroidsByToday()
        }
    }

    fun getAsteroidsBySave(){
        viewModelScope.launch {
            _asteroidList.value=repository.getAsteroidsBySave()
        }
    }

    init {
        viewModelScope.launch {
            _asteroidList.value=repository.getAsteroidsByWeek()
        }
        refreshAsteroids()
        refreshPicture()
    }


    fun displayDetails(asteroid: Asteroid){
        _navigateToDetails.value=asteroid
    }

    fun displayDetailsFinished(){
        _navigateToDetails.value=null
    }

    fun checkNetworkAndRefresh()
    {
        //_connectToNetwork.value=false
        refreshAsteroids()
        refreshPicture()
    }

    fun refreshPicture(){
        //checKNetwork()
        if(checKNetwork(application))
        {
            viewModelScope.launch {
                _pictureOfDay.value=repository.refreshPicture()
            }
        }
    }

    fun refreshAsteroids(){
        //checKNetwork()
        if(checKNetwork(application)) {
            viewModelScope.launch {
                repository.refreshAsteroids()
            }
        }
        else{
            Toast.makeText(application,"No network connection",Toast.LENGTH_SHORT).show()
        }
    }







}