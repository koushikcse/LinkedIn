package com.kusu.sample

import android.content.Context
import android.widget.Toast

fun Context.showText(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

