package com.rdpsoftware.myappointments.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.rdpsoftware.myappointments.Models.Appointment
import com.rdpsoftware.myappointments.databinding.ActivityAppointmentsBinding

class AppointmentsActivity : AppCompatActivity() {
    private lateinit var bindings:ActivityAppointmentsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        bindings = ActivityAppointmentsBinding.inflate(this.layoutInflater)
        val view = bindings.root
        super.onCreate(savedInstanceState)
        setContentView(view)

        loadData()
    }

    private fun loadData(){
        val appointments = ArrayList<Appointment>()
        appointments.add(Appointment(1,"Medico Test2","03/29/2021","9:30 AM"))
        appointments.add(Appointment(3,"Medico Test2","03/29/2021","1:30 PM"))
        appointments.add(Appointment(4,"Medico Test1","03/29/2021","2:00 PM"))

        bindings.rvAppointments.layoutManager = LinearLayoutManager(this)
        bindings.rvAppointments.adapter = AppointmentAdapter(appointments)
    }
}