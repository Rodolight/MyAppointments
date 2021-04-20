package com.rdpsoftware.myappointments.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.rdpsoftware.myappointments.Models.Appointment
import com.rdpsoftware.myappointments.databinding.ActivityAppointmentsBinding
import com.rdpsoftware.myappointments.io.ApiService
import com.rdpsoftware.myappointments.utils.PreferenceHelper
import com.rdpsoftware.myappointments.utils.PreferenceHelper.get
import com.rdpsoftware.myappointments.utils.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AppointmentsActivity : AppCompatActivity() {
    private lateinit var bindings:ActivityAppointmentsBinding
    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private val preferences by lazy {
      PreferenceHelper.defaultPrefs(this)
    }

    private val appointmentAdapter = AppointmentAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        bindings = ActivityAppointmentsBinding.inflate(this.layoutInflater)
        val view = bindings.root
        super.onCreate(savedInstanceState)
        setContentView(view)

        loadAppointments()

        bindings.rvAppointments.layoutManager = LinearLayoutManager(this)
        bindings.rvAppointments.adapter = appointmentAdapter

       }

    private fun loadAppointments(){
        val jwt = preferences["jwt", ""]
       val call = apiService.getAppointments("Bearer $jwt")
       call.enqueue(object:Callback<ArrayList<Appointment>> {
           override fun onResponse(call: Call<ArrayList<Appointment>>, response: Response<ArrayList<Appointment>>) {
             if ( response.isSuccessful){
                 response.body()?.let {
                     appointmentAdapter.appointments = it
                     appointmentAdapter.notifyDataSetChanged()
                 }
             }
           }

           override fun onFailure(call: Call<ArrayList<Appointment>>, t: Throwable) {
              toast(t.localizedMessage)
           }

       }) ;


    }
}