package com.rdpsoftware.myappointments.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rdpsoftware.myappointments.R
import com.rdpsoftware.myappointments.databinding.ActivityRegisterBinding
import com.rdpsoftware.myappointments.io.ApiService
import com.rdpsoftware.myappointments.io.response.LoginResponse
import com.rdpsoftware.myappointments.utils.PreferenceHelper
import com.rdpsoftware.myappointments.utils.PreferenceHelper.set
import com.rdpsoftware.myappointments.utils.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var bindings : ActivityRegisterBinding

    private val apiService by lazy{
        ApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindings = ActivityRegisterBinding.inflate(this.layoutInflater)
        val view = bindings.root
        setContentView(view)

        bindings.tvGoToLogin.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        bindings.btnRegister.setOnClickListener{
            performRegister()
        }

    }

    private fun performRegister(){
        val name = bindings.etRegisterName.text.toString().trim()
        val email = bindings.etRegisterEmail.text.toString().trim()
        val password = bindings.etRegisterPassword.text.toString()
        val passwordConfirmation = bindings.etRegisterPasswordConfirmation.text.toString()

        if(name.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirmation.isEmpty()){
            toast(getString(R.string.error_register_empty_fields))
            return
        }

        if(password != passwordConfirmation){
            toast(getString(R.string.error_register_passwords_do_dot_match))
        }

        val call = apiService.postRegister(name,email,password,passwordConfirmation)
        call.enqueue(object :Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                  val loginResponse = response.body()
                    if (loginResponse == null) {
                        toast(getString(R.string.error_login_response))
                        return
                    }
                    if(loginResponse.success){
                        createSessionPreferences(loginResponse.jwt)
                        toast(getString(R.string.welcome_name,loginResponse.user.name))
                        goToMenuActivity()
                    }else{
                        toast(getString(R.string.error_invalid_credentials))
                    }
                }else{
                    toast(getString(R.string.error_register_validation))
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

    private fun goToMenuActivity(){
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

}