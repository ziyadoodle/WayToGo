package com.wtg.waytogo.data.source

import com.wtg.waytogo.data.response.PlaceResponse
import com.wtg.waytogo.data.response.SignInResponse
import com.wtg.waytogo.data.response.SignUpResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("signin")
    suspend fun signIn(
        @Field("email") email: String,
        @Field("password") password: String
    ): SignInResponse

    @FormUrlEncoded
    @POST("signup")
    suspend fun signUp(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): SignUpResponse

    @GET("place")
    suspend fun getPlace(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1
    ): PlaceResponse

    @GET("place/{place_id}")
    suspend fun getDetailPlace(
        @Header("Authorization") token: String,
        @Path("place_id") id: String
    )

}