package com.sagarmishra.futsal.Repository

import com.sagarmishra.futsal.api.ApiRequest
import com.sagarmishra.futsal.api.AuthAPI
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.entityapi.AuthUser
import com.sagarmishra.futsal.response.APIResponse
import com.sagarmishra.futsal.response.LoginResponse
import com.sagarmishra.futsal.response.PasswordResetResponse
import com.sagarmishra.futsal.response.SignUpResponse
import okhttp3.MultipartBody

class UserRepository():ApiRequest() {
    val authService = RetrofitService.retroService(AuthAPI::class.java)
    suspend fun registerUser(user:AuthUser):SignUpResponse
    {
        return apiRequest {
                authService.registerUser(user)
            }

    }

    suspend fun authenticateUser(username:String,password:String):LoginResponse
    {
        return apiRequest {
            authService.authenticateUser(username,password)
        }
    }

    suspend fun forgotPassword(email:String):PasswordResetResponse
    {
        return apiRequest {
            authService.forgotPassword(email)
        }
    }

    suspend fun resetPassword(newPassword:String,confirmPassword:String):APIResponse
    {
        return apiRequest {
            authService.resetPassword(newPassword,confirmPassword,RetrofitService.resetToken)
        }
    }

    suspend fun addProfilePic(id:String,profilePic:MultipartBody.Part):APIResponse
    {
        return apiRequest {
            authService.addProfilePicture(id,profilePic)
        }
    }

    suspend fun updateDetails(user:AuthUser):APIResponse
    {
        return apiRequest {
            authService.updateDetails(RetrofitService.token!!,user)
        }
    }

    suspend fun changeProfilePicture(body:MultipartBody.Part):APIResponse
    {
        return apiRequest {
            authService.changeProfilePicture(RetrofitService.token!!,body)
        }
    }

    suspend fun noImage():APIResponse
    {
        return apiRequest {
            authService.noImage(RetrofitService.token!!)
        }
    }

    suspend fun getUser():LoginResponse
    {
        return apiRequest {
            authService.getUser(RetrofitService.token!!)
        }
    }

    suspend fun changePassword(current:String,new:String,confirm:String):APIResponse
    {
        return apiRequest {
            authService.changePassword(RetrofitService.token!!,current,new,confirm)
        }
    }

    suspend fun verifyReset(pin:String):PasswordResetResponse
    {
        return apiRequest {
            authService.verifyPasswordResetCode(RetrofitService.resetToken!!,pin)
        }
    }
}