package com.rdpsoftware.myappointments.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.rdpsoftware.myappointments.Models.User
import com.rdpsoftware.myappointments.R
import com.rdpsoftware.myappointments.databinding.ActivityProfileBinding
import com.rdpsoftware.myappointments.io.ApiService
import com.rdpsoftware.myappointments.utils.getAuthHeader
import com.rdpsoftware.myappointments.utils.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {
    private lateinit var bindings: ActivityProfileBinding;
    private val apiService by lazy {
        ApiService.create()
    }

//    private val preferences by lazy {
//        PreferenceHelper.defaultPrefs(this)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindings = ActivityProfileBinding.inflate(this.layoutInflater)
        val view = bindings.root
        setContentView(view)

        loadProfileData()

        /*Handler(Looper.getMainLooper()).postDelayed({
          displayProfileData()
        }, 3000)*/
    }

    private fun loadProfileData(){

        val authHeader = getAuthHeader(this)

        val call = apiService.getUser(authHeader)
        call.enqueue(object: Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    val user = response.body()
                    if (user != null) {
                        displayProfileData(user)
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                toast(t.localizedMessage)
            }
        })

    }

    private fun displayProfileData(user: User){
        bindings.etName.setText(user.name)
        bindings.etPhone.setText(user.phone)
        bindings.etAddress.setText(user.address)

        bindings.progressBarProfile.visibility = View.GONE
        bindings.linearLayoutProfile.visibility = View.VISIBLE

        bindings.btnSave.setOnClickListener{
            saveProfile()
        }
    }

    private fun saveProfile(){

        val name = bindings.etName.text.toString()
        val phone = bindings.etPhone.text.toString()
        val address = bindings.etAddress.text.toString()

        if(name.length <= 4){
            bindings.inputLayoutName.error = getString(R.string.error_profile_name)
            return
        }

        val authHeader = getAuthHeader(this)

        val call = apiService.postUser(authHeader,name,phone,address)
        call.enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    toast(getString(R.string.profile_success_message))
                    finish()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
               toast(t.localizedMessage)
            }

        })
    }
}