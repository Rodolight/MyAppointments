package com.rdpsoftware.myappointments.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rdpsoftware.myappointments.PreferenceHelper
import com.rdpsoftware.myappointments.PreferenceHelper.set
import com.rdpsoftware.myappointments.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

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
            clearSessionPreferences()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun clearSessionPreferences(){
       /* val preferences = getSharedPreferences("general", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("session", false)
        editor.apply()*/

        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["session"] = false
    }
}