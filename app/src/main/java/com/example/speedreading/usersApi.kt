package com.example.speedreading

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface usersApi {
    @GET("users/get")
    fun getAllUsers(): Call<List<users>>

    @POST("users/save")
    fun save(@Body users: users): Call<users>
}