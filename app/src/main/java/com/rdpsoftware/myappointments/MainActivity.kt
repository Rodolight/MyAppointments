 package com.rdpsoftware.myappointments

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.rdpsoftware.myappointments.databinding.ActivityMainBinding
import com.rdpsoftware.myappointments.PreferenceHelper.get
import com.rdpsoftware.myappointments.PreferenceHelper.set

 class MainActivity : AppCompatActivity() {
     private lateinit var binding: ActivityMainBinding
     private val snackBar by lazy {
         Snackbar.make(binding.mainLayout, R.string.press_back_again, Snackbar.LENGTH_LONG)
     }

         override fun onCreate(savedInstanceState: Bundle?) {
             super.onCreate(savedInstanceState)
             binding = ActivityMainBinding.inflate(this.layoutInflater)
             val view = binding.root
             setContentView(view)
             //shared preferences
//         val preferences = getSharedPreferences("general", MODE_PRIVATE)
//         val session = preferences.getBoolean("session", false)
             val preferences = PreferenceHelper.defaultPrefs(this)

             if(preferences["session", false])
                 goToMenuActivity()

             binding.btnLogin.setOnClickListener(){
                 // validates
                 createSessionPreferences()
                 goToMenuActivity()
             }

             binding.tvGoToRegister.setOnClickListener(){
                 //Toast.makeText(this, getString(R.string.please_fill_your_register_data), Toast.LENGTH_SHORT).show()
                 val intent = Intent(this, RegisterActivity::class.java)
                 startActivity(intent)
             }
         }

         private fun createSessionPreferences(){
//         val preferences = getSharedPreferences("general", MODE_PRIVATE)
//         val editor = preferences.edit()
//         editor.putBoolean("session", true)
//         editor.apply()
             val preferences = PreferenceHelper.defaultPrefs(this)
             preferences["session"] = true
         }
         private fun goToMenuActivity(){
             val intent = Intent(this, MenuActivity::class.java)
             startActivity(intent)
             finish()
         }

         override fun onBackPressed() {
             if (snackBar.isShown)
                 super.onBackPressed()
             else
                 snackBar.show()
         }
 }