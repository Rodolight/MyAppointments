package com.rdpsoftware.myappointments.Models

import com.google.gson.annotations.SerializedName

data class Appointment (val id: Int,
                        val description: String,
                        val type: String,
                        val status: String,

                        @SerializedName("schedule_date") val scheduledDate: String,
                        @SerializedName("schedule_time_12") val scheduleTime: String,
                        @SerializedName("created_at") val createdAt: String,

                        val specialty : Specialty,
                        val doctor: Doctor )

