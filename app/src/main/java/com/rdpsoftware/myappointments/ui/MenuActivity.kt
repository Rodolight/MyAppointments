package com.rdpsoftware.myappointments.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rdpsoftware.myappointments.utils.PreferenceHelper
import com.rdpsoftware.myappointments.utils.PreferenceHelper.set
import com.rdpsoftware.myappointments.utils.PreferenceHelper.get
import com.rdpsoftware.myappointments.databinding.ActivityMenuBinding
import com.rdpsoftware.myappointments.io.ApiService
import com.rdpsoftware.myappointments.utils.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    private val apiService by lazy {
        ApiService.create()
    }

    private val preferences by lazy {
         PreferenceHelper.defaultPrefs(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(this.layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnCreateAppointment.setOnClickListener{
            val intent = Intent(this, CreateAppointmentActivity::class.java)
            startActivity(intent)
        }

        binding.btnMyAppointment.setOnClickListener{
            val intent = Intent(this, AppointmentsActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogOut.setOnClickListener{
            performLogout()
        }
    }

    private fun performLogout(){
        val jwt = preferences["jwt", ""]
        val call = apiService.postLogout("Bearer $jwt")
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                clearSessionPreferences()
                val intent = Intent(this@MenuActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
               toast(t.localizedMessage)
            }

        })
    }

    private fun clearSessionPreferences(){
        preferences["jwt"] = ""
    }
}