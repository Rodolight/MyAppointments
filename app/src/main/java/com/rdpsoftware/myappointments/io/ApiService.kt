package com.rdpsoftware.myappointments.io

import com.rdpsoftware.myappointments.Models.Specialty
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("specialties")
    abstract fun getSpecialties():Call<ArrayList<Specialty>>

    companion object Factory{
        private const val BASE_URL = "http://104.236.89.149/api/"

        fun create(): ApiService{
          val retrofit = Retrofit.Builder()
              .baseUrl(BASE_URL)
              .addConverterFactory(GsonConverterFactory.create())
              .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}