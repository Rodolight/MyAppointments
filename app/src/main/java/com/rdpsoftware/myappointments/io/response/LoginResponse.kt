package com.rdpsoftware.myappointments.io.response

import com.rdpsoftware.myappointments.Models.User

data class LoginResponse(val success: Boolean, val user: User, val jwt: String)