package com.example.speedreading

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface performanceApi {
    @GET("/performance/get")
    fun getAllPerformance(): Call<List<performance>>

    @POST("/performance/save")
    fun save(@Body performance: performance): Call<performance>
}