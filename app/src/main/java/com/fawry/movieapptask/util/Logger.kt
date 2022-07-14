package com.fawry.movieapptask.util

import android.content.ContentValues.TAG
import android.util.Log
import com.fawry.movieapptask.BuildConfig.DEBUG

var isUnitTest = false

fun printLogD(className: String?, message: String ) {
    if (DEBUG && !isUnitTest) {
        Log.d(TAG, "$className: $message")
    }
    else if(DEBUG && isUnitTest){
        println("$className: $message")
    }
}

/*
    Priorities: Log.DEBUG, Log. etc....
 */
fun cLog(msg: String?){
    msg?.let {
        if(!DEBUG){
          //  FirebaseCrashlytics.getInstance().log(it)
        }
    }

}
