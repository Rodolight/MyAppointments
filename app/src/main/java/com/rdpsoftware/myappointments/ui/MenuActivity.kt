package com.rdpsoftware.myappointments.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.rdpsoftware.myappointments.Models.User
import com.rdpsoftware.myappointments.R
import com.rdpsoftware.myappointments.utils.PreferenceHelper
import com.rdpsoftware.myappointments.utils.PreferenceHelper.set
import com.rdpsoftware.myappointments.utils.PreferenceHelper.get
import com.rdpsoftware.myappointments.databinding.ActivityMenuBinding
import com.rdpsoftware.myappointments.io.ApiService
import com.rdpsoftware.myappointments.utils.getAuthHeader
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

    private val authHeader by lazy {
        getAuthHeader(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(this.layoutInflater)
        val view = binding.root
        setContentView(view)

        val storeToken = intent.getBooleanExtra("store_token", false)
        if(storeToken) storeToken()

        binding.btnCreateAppointment.setOnClickListener{
           createAppointment(it)
        }

        binding.btnMyAppointment.setOnClickListener{
            val intent = Intent(this, AppointmentsActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogOut.setOnClickListener{
            performLogout()
        }
    }

    private fun createAppointment(view: View){
        val call = apiService.getUser(authHeader)
        call.enqueue(object: Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    val user = response.body()
                    if (user != null) {
                        if(user.phone.length >=7){
                            val intent = Intent(this@MenuActivity, CreateAppointmentActivity::class.java)
                            startActivity(intent)
                        }else{
                          Snackbar.make(view, R.string.you_need_a_phone, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
               toast(t.localizedMessage)
            }

        } )



    }

    fun editProfile(view: View ){
     val intent = Intent(this,ProfileActivity::class.java)
     startActivity(intent)
    }

    private fun storeToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCMService", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val deviceToken = task.result

            if (deviceToken != null) {
                val call = apiService.postToken(authHeader, deviceToken)
                call.enqueue(object:Callback<Void>{
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if(response.isSuccessful){
                            Log.d(Companion.TAG,"Token registrado correctamente.")
                        }else{
                            Log.d(Companion.TAG,"Hubo un problema al registrar el token.")
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        toast(t.localizedMessage)
                    }

                })
            }
        })
    }

    private fun performLogout(){
        val call = apiService.postLogout(authHeader)
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

    companion object {
        private const val TAG = "MenuActivity"
    }
}