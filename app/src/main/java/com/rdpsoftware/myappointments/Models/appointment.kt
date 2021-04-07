package com.rdpsoftware.myappointments.Models

data class Appointment (
    val id: Int,
    val doctorName: String,
    val scheduledDate: String,
    val ScheduledTime:String)
