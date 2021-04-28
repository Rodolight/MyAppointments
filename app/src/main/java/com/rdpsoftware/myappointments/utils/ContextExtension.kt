package com.rdpsoftware.myappointments.utils

import android.content.Context
import android.widget.Toast
import com.rdpsoftware.myappointments.utils.PreferenceHelper.get

fun Context.toast(message: CharSequence) = Toast.makeText(this,message,Toast.LENGTH_SHORT).show()

fun getAuthHeader(context: Context): String {
    var preferences = PreferenceHelper.defaultPrefs(context)
    val jwt = preferences["jwt",""]
    return "Bearer $jwt"
}