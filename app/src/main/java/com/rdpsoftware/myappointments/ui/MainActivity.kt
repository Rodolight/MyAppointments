 package com.rdpsoftware.myappointments.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.rdpsoftware.myappointments.R
import com.rdpsoftware.myappointments.databinding.ActivityMainBinding
import com.rdpsoftware.myappointments.io.ApiService
import com.rdpsoftware.myappointments.io.response.LoginResponse
import com.rdpsoftware.myappointments.utils.PreferenceHelper
import com.rdpsoftware.myappointments.utils.PreferenceHelper.get
import com.rdpsoftware.myappointments.utils.PreferenceHelper.set
import com.rdpsoftware.myappointments.utils.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

 class MainActivity : AppCompatActivity()  {
     private lateinit var binding: ActivityMainBinding
     private val snackBar by lazy {
         Snackbar.make(binding.mainLayout, R.string.press_back_again, Snackbar.LENGTH_LONG)
     }

     private val apiService: ApiService by lazy{
         ApiService.create()
     }
         override fun onCreate(savedInstanceState: Bundle?) {
             super.onCreate(savedInstanceState)
             binding = ActivityMainBinding.inflate(this.layoutInflater)
             val view = binding.root
             setContentView(view)

             FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                 if (!task.isSuccessful) {
                     Log.w("FCMService", "Fetching FCM registration token failed", task.exception)
                     return@OnCompleteListener
                 }

                 // Get new FCM registration token
                 val token = task.result

                 // Log and toast
                 //val msg = getString(R.string.fcm_message, token)
                 if (token != null) {
                     Log.d("FCMService", token)
                 }
                // Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
             })

             //shared preferences
//         val preferences = getSharedPreferences("general", MODE_PRIVATE)
//         val session = preferences.getBoolean("session", false)
             val preferences = PreferenceHelper.defaultPrefs(this)

             if(preferences["jwt", ""].contains("."))
                 goToMenuActivity()

             binding.btnLogin.setOnClickListener(){
                 // validates
                 performLogin()
             }

             binding.tvGoToRegister.setOnClickListener(){
                 //Toast.makeText(this, getString(R.string.please_fill_your_register_data), Toast.LENGTH_SHORT).show()
                 val intent = Intent(this, RegisterActivity::class.java)
                 startActivity(intent)
             }
         }

         private fun performLogin(){
             val email = binding.etEmail.text.toString()
             val password = binding.etPassword.text.toString()

             if(email.trim().isEmpty() || password.trim().isEmpty()){
               toast(getString(R.string.error_empty_credentials))
                 return
             }

            val call = apiService.postLogin(email, password )
            call.enqueue(object: Callback<LoginResponse>{
                override fun onResponse( call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if(response.isSuccessful){
                        val loginResponse = response.body()
                            if (loginResponse == null) {
                                toast(getString(R.string.error_login_response))
                                return
                            }
                        if(loginResponse.success){
                            createSessionPreferences(loginResponse.jwt)
                            toast(getString(R.string.welcome_name,loginResponse.user.name))
                            goToMenuActivity(true)
                        }else{
                            toast(getString(R.string.error_invalid_credentials))
                        }
                    }else{
                        toast(getString(R.string.error_login_response))
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                  toast(t.localizedMessage)
                }

            })
        }

         private fun createSessionPreferences(jwt : String){
             val preferences = PreferenceHelper.defaultPrefs(this)
             preferences["jwt"] = jwt
         }

         private fun goToMenuActivity( isUserInput: Boolean = false){
              val intent = Intent(this, MenuActivity::class.java)
             if(isUserInput){
               intent.putExtra("store_token", true)
             }
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