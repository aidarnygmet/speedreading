package com.example.speedreading
import android.util.Log
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class retrofit {
    lateinit var retrofit: Retrofit
    fun retrofitinit(){
        retrofit = Retrofit.Builder().baseUrl("http://192.168.1.70:8080").addConverterFactory(GsonConverterFactory.create()).build()
    }


}