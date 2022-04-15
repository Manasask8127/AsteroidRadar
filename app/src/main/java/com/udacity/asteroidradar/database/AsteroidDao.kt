package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            ).addCallback(roomCallback)
                .build()

        }
    }
    return INSTANCE
}

private val roomCallback=object :RoomDatabase.Callback(){
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        GlobalScope.launch { addDummyContent() }
    }
}

suspend fun addDummyContent(){
    withContext(Dispatchers.IO){
        INSTANCE.asteroidDao.insertAll(*getDummyContents().toTypedArray())
    }
}
private fun getDummyContents():ArrayList<DatabaseAsteroid>{
    val asteroid1=DatabaseAsteroid(
        id=2465633,
        codename = "465633 (2009 JRS)",
        closeApproachDate = "2015-09-08",
        absoluteMagnitude = 20.36,
        estimatedDiameter = 0.5035469604,
        relativeVelocity = 18.1279547773,
        distanceFromEarth = 0.3027478814,
        isPotentiallyHazardous = true
    )
    val asteroid2=DatabaseAsteroid(
        id=3426410,
        codename = " (2008 QV11)",
        closeApproachDate = "2015-09-08",
        absoluteMagnitude = 20.36,
        estimatedDiameter = 0.3232469604,
        relativeVelocity = 19.1279547773,
        distanceFromEarth = 0.2507478814,
        isPotentiallyHazardous = false
    )
    return arrayListOf(asteroid1,asteroid2)
}