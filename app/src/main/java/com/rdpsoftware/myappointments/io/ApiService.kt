package com.rdpsoftware.myappointments.io

import com.rdpsoftware.myappointments.Models.Appointment
import com.rdpsoftware.myappointments.Models.Doctor
import com.rdpsoftware.myappointments.Models.Schedule
import com.rdpsoftware.myappointments.Models.Specialty
import com.rdpsoftware.myappointments.io.response.LoginResponse
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface ApiService {
    @GET("specialties")
    abstract fun getSpecialties():Call<ArrayList<Specialty>>

    @GET("specialties/{specialty}/doctors")
    abstract fun getDoctors(@Path("specialty") specialtyId: Int):Call<ArrayList<Doctor>>

    @GET("schedule/hours")
    abstract fun getHours(@Query("doctor_id") doctor_id: Int, @Query("date") date: String):Call<Schedule>

    @POST("login")
    abstract fun postLogin(@Query("email") email: String, @Query("password") password: String):Call<LoginResponse>

    @POST("logout")
    abstract fun postLogout(@Header("Authorization") authHeader: String):Call<Void>

    @GET("appointments")
    abstract fun getAppointments(@Header("Authorization") authHeader: String):Call<ArrayList<Appointment>>

    companion object Factory{
        private const val BASE_URL = "http://104.236.89.149/api/"

        fun create(): ApiService{
           val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

          val retrofit = Retrofit.Builder()
                  .baseUrl(BASE_URL)
                  .addConverterFactory(GsonConverterFactory.create())
                  .client(client)
                  .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}