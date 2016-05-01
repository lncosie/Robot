package com.lncosie.robot.net

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * Created by lncosie on 2016/5/1.
 */
interface FriendService{
    class User{

    }
    @Headers(
        "Accept: application/vnd.github.v3.full+json",
        "User-Agent: Retrofit-Sample-App"
    )
    @GET("users/{username}")
    fun getUser(@Path("username")  username:String): Call<User>
}
/*Retrofit retrofit = new Retrofit.Builder()
    .baseUrl("https://api.github.com")
    .addConverterFactory(GsonConverterFactory.create())
    .build();
* */