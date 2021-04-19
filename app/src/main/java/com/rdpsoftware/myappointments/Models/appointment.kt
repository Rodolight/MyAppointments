package com.rdpsoftware.myappointments.Models

import com.google.gson.annotations.SerializedName

/*

"id": 18,
        "description": "Qui sit non esse consequatur illo aut.",
        "schedule_date": "2021-02-24",
        "type": "Operación",
        "created_at": "2021-04-11T20:29:04.000000Z",
        "status": "Cancelada",
        "schedule_time_12": "2:40 AM",
        "specialty": {
            "id": 3,
            "name": "Urología"
        },
        "doctor": {
            "id": 45,
            "name": "Stanton Koelpin MD"
        }
    }

*/


data class Appointment (
        val id: Int,
        val description: String,
        val type: String,
        val status: String,

        @SerializedName("schedule_date") val scheduledDate: String,
        @SerializedName("schedule_time_12") val scheduleTime: String,
        @SerializedName("created_at") val createdAt: String,

        val specialty : Specialty,
        val doctor: Doctor
)
