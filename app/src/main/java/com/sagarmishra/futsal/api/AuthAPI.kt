package com.sagarmishra.futsal.api

import com.sagarmishra.futsal.entityapi.AuthUser
import com.sagarmishra.futsal.response.APIResponse
import com.sagarmishra.futsal.response.LoginResponse
import com.sagarmishra.futsal.response.PasswordResetResponse
import com.sagarmishra.futsal.response.SignUpResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface AuthAPI {
    @POST("register/user")
    suspend fun registerUser(@Body user: AuthUser): Response<SignUpResponse>

    @FormUrlEncoded
    @POST("user/login")
    suspend fun authenticateUser(
        @Field("un") username: String = "",
        @Field("pw") password: String = ""
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("getEmailAddressAndSendCode")
    suspend fun forgotPassword(@Field("email") email: String? = null): Response<PasswordResetResponse>

    @FormUrlEncoded
    @POST("resetUserPassword")
    suspend fun resetPassword(
        @Field("newPassword") pid: String? = null,
        @Field("confirmPassword") password: String? =null,
        @Field("token") token:String?=null
    ): Response<APIResponse>

    @Multipart
    @PUT("addProfilePicture/{pid}")
    suspend fun addProfilePicture(
        @Path("pid") id:String,
        @Part profilePic:MultipartBody.Part
    ):Response<APIResponse>

    @POST("update/details")
    suspend fun updateDetails(
        @Header("Authorization") token:String,
        @Body user:AuthUser
    ):Response<APIResponse>

    @Multipart
    @PUT("change/profilePicture")
    suspend fun changeProfilePicture(
        @Header("Authorization") token:String,
        @Part profileImg:MultipartBody.Part
    ):Response<APIResponse>

    @PUT("noImage/update")
    suspend fun noImage(
        @Header("Authorization") token:String
    ):Response<APIResponse>

    @GET("getUser")
    suspend fun getUser(
        @Header("Authorization") token:String
    ):Response<LoginResponse>

    @FormUrlEncoded
    @POST("change/password")
    suspend fun changePassword(
        @Header("Authorization") token:String,
        @Field("currentPassword") current:String,
        @Field("newPassword") new:String,
        @Field("confirmPassword") confirm:String

    ):Response<APIResponse>

    @FormUrlEncoded
    @POST("verifyPinCode")
    suspend fun verifyPasswordResetCode(
        @Field("token") token:String,
        @Field("pinCode") pin:String
    ):Response<PasswordResetResponse>
}