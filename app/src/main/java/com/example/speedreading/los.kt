package com.example.speedreading

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.Calendar
import java.util.Date


class los : AppCompatActivity() {
    private lateinit var retrofitButton: Button
    var retrofitService: retrofit = retrofit()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("test", "init3")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_los)
        retrofitButton = findViewById(R.id.retrofit)
        var performanceApi = Retrofit.Builder().baseUrl("http://192.168.1.70:8080").addConverterFactory(
            GsonConverterFactory.create()).build().create(com.example.speedreading.performanceApi::class.java)
        retrofitButton.setOnClickListener {
            val performance = performance(9, 9, 9,Calendar.getInstance().time.toString())
            GlobalScope.launch(Dispatchers.IO) {

                    val response = performanceApi.save(performance).awaitResponse()
                    if (response.isSuccessful) {
                        val data = response.body()!!
                        Log.d("test", data.toString())
                    } else {
                        val data = response.errorBody()!!
                        Log.d("test", "hui")
                    }

            }
        }
    }

}