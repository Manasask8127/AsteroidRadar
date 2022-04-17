package com.udacity.asteroidradar

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.*


fun getToday():String{
    val calendar=Calendar.getInstance()
    return formateDate(calendar.time)
}

private fun formateDate(date:Date):String{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT,Locale.getDefault()).format(date)
    } else {
        TODO("VERSION.SDK_INT < N")
    }

}


fun getSeventhDay():String{
    val calendar=Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR,7)
    return formateDate(calendar.time)
}