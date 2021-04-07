package com.rdpsoftware.myappointments

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rdpsoftware.myappointments.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var bindings : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindings = ActivityRegisterBinding.inflate(this.layoutInflater)
        val view = bindings.root
        setContentView(view)

        loadData()

    }

    private fun loadData(){
        bindings.tvGoToLogin.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}