package com.example.innocvkotlintask.network

import com.example.innocvkotlintask.data.UserModel
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

    @GET("User")
    fun getUsers(): Call<List<UserModel>>

    @GET("User/{id}")
    fun getUserById(@Path("id") id: Int): Call<UserModel>

    @POST("User")
    fun addUser(@Body userModel: UserModel): Call<UserModel>

    @PUT("User")
    fun modifyUser(@Body userModel: UserModel?): Call<UserModel>

    @DELETE("User/{id}")
    fun deleteUser(@Path("id") id: Int): Call<String>

}