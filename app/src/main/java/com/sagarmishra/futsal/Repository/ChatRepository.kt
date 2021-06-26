package com.sagarmishra.futsal.Repository

import com.sagarmishra.futsal.api.ApiRequest
import com.sagarmishra.futsal.api.ChatAPI
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.response.ChatResponse
import com.sagarmishra.futsal.response.SingleChatResponse
import com.sagarmishra.futsal.response.TeamRequireResponse
import com.sagarmishra.futsal.response.UnseenResponse

class ChatRepository():ApiRequest() {
    val chatAPI = RetrofitService.retroService(ChatAPI::class.java)

    suspend fun teamRequiredPlayers():TeamRequireResponse
    {
        return apiRequest {
            chatAPI.teamRequiredPlayers(RetrofitService.token!!)
        }
    }

    suspend fun myInteractions():TeamRequireResponse
    {
        return apiRequest {
            chatAPI.myInteractions(RetrofitService.token!!)
        }
    }

    suspend fun fetchMyUnseen():UnseenResponse
    {
        return apiRequest {
            chatAPI.fetchMyUnseen(RetrofitService.token!!)
        }
    }

    suspend fun getChatMessages(rid:String):ChatResponse
    {
        return apiRequest {
            chatAPI.getChatMessages(RetrofitService.token!!,rid)
        }
    }

    suspend fun sendMessage(rid:String,message:String):SingleChatResponse
    {
        return apiRequest {
            chatAPI.sendMessage(RetrofitService.token!!,rid,message)
        }
    }
}