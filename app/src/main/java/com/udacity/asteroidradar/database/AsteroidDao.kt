package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao{

    @Query("select * from databaseasteroid order by closeApproachDate ASC")
    fun getAsteroids() : LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids:DatabaseAsteroid)

    //Add-on
    @Query("delete from databaseasteroid where closeApproachDate<:presentDate")
    fun deletePastAsteroids(presentDate : String)

}

@Database(entities = [DatabaseAsteroid::class],version = 1)
abstract class AsteroidDatabase:RoomDatabase()
{
    abstract val asteroidDao:AsteroidDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                "videos"
            ).build()
        }
    }
    return INSTANCE
}