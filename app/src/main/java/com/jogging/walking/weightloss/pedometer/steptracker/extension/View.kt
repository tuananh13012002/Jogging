package com.jogging.walking.weightloss.pedometer.steptracker.extension

import android.animation.ObjectAnimator
import android.content.Context
import android.os.SystemClock
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.jogging.walking.weightloss.pedometer.steptracker.R

fun View.beGone(){
    this.visibility = View.GONE
}

fun View.beVisible(){
    this.visibility = View.VISIBLE
}

fun View.beVisibleParam(visible: Boolean = true) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.beInvisible(){
    this.visibility = View.INVISIBLE
}

fun View.alpha0(){
    this.alpha = 0F
}
fun View.alpha1(){
    this.alpha = 1F
}

fun TextView.setColorResource(resId: Int){
    setTextColor(ContextCompat.getColor(context, resId))
}
fun View.onClickListener(onClick:(View) -> Unit){
    setOnClickListener {
        if (it.isEnabled){
            it.isEnabled = false
            onClick(this)
            it.postDelayed({it.isEnabled = true}, 500)
        }
    }
}
fun View.hideKeyboard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)?.hideSoftInputFromWindow(
        this.windowToken,
        0
    )
}

fun View.showKeyboard(){
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)?.showSoftInput(
        this,
        InputMethodManager.SHOW_IMPLICIT
    )
}


fun ImageView.setColorResource(idColor:Int){
    setColorFilter(ContextCompat.getColor(context,idColor))
}

fun View.onEventClickListener(onClick:(View)->Unit){
    if (this.isEnabled){
        this.isEnabled = false
        setOnClickListener {
            onClick(it)
        }
        this.postDelayed({this.isEnabled = true}, 400)
    }
}

fun View.startRotateAnim(){
    val rotateAnim = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f)
    rotateAnim?.apply {
        duration = 3500
        repeatCount = ObjectAnimator.INFINITE
        interpolator = LinearInterpolator()
    }
    rotateAnim.start()
}