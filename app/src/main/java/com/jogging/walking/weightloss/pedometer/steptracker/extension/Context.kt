package com.jogging.walking.weightloss.pedometer.steptracker.extension

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.MusicModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

fun Context.hasPermission(permissions: Array<String>): Boolean{
    var granted = true
    for (per in permissions){
        if (ActivityCompat.checkSelfPermission(this, per) == PackageManager.PERMISSION_DENIED){
            granted = false
            break
        }
    }
    return granted
}

fun Context.toast(stringRes: Int, length: Int = Toast.LENGTH_SHORT){
    toast(getString(stringRes), length)
}

fun Context.toast(msg: String, length: Int = Toast.LENGTH_SHORT){
    CoroutineScope(Dispatchers.Main).launch {
        doToast(this@toast, msg, length)
    }
}

private fun doToast(context: Context, msg: String, length: Int){
    if (context is Activity){
        if (!context.isDestroyed && !context.isFinishing){
            Toast.makeText(context, msg, length).show()
        }
    }else{
        Toast.makeText(context, msg, length).show()
    }
}

fun Context.readMusicJSONFromAsset(): ArrayList<MusicModel>{
    return try {
        val jsonString = assets.open("jogging_music.json").bufferedReader().use { it.readText() }
        val itemType = object : TypeToken<ArrayList<MusicModel>>() {}.type
        Gson().fromJson(jsonString, itemType)
    }catch (ex: Exception){
        ex.printStackTrace()
        arrayListOf()
    }
}


