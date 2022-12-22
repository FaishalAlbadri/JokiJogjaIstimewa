package com.fanonymous.jogjaistimewa.api

import com.fanonymous.jogjaistimewa.data.BaseResponse
import com.fanonymous.jogjaistimewa.data.destinasi.DestinasiResponse
import com.fanonymous.jogjaistimewa.data.user.UserResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIInterface {

    @FormUrlEncoded
    @POST("favorite/add.php")
    fun favorite(
        @Field("id_user") id_user: String,
        @Field("id_destinasi") id_destinasi: String,
        @Field("destinasi_kategori") destinasi_kategori: String
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST("destinasi/get.php")
    fun destinasi(
        @Field("destinasi_kategori") destinasi_kategori: String
    ): Call<DestinasiResponse>

    @FormUrlEncoded
    @POST("user/profile.php")
    fun profile(
        @Field("id_user") id_user: String
    ): Call<UserResponse>


    @FormUrlEncoded
    @POST("user/login.php")
    fun login(
        @Field("user_email") user_email: String,
        @Field("user_password") user_password: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("user/register.php")
    fun register(
        @Field("user_name") user_name: String,
        @Field("user_email") user_email: String,
        @Field("user_password") user_password: String
    ): Call<BaseResponse>
}