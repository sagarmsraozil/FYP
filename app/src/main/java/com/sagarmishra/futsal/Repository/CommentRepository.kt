package com.sagarmishra.futsal.Repository

import com.sagarmishra.futsal.api.ApiRequest
import com.sagarmishra.futsal.api.CommentAPI
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.entityapi.FutsalComment
import com.sagarmishra.futsal.response.FutsalCommentResponse

class CommentRepository():ApiRequest() {
    val commentAPI = RetrofitService.retroService(CommentAPI::class.java)

    suspend fun addComment(id:String,comment:String):FutsalCommentResponse
    {
        return apiRequest {
            commentAPI.addComment(RetrofitService.token!!,id,comment)
        }
    }

    suspend fun retrieveComments(id:String):FutsalCommentResponse
    {
        return apiRequest {
            commentAPI.retrieveComments(id)
        }
    }

    suspend fun deleteComment(id:String):FutsalCommentResponse
    {
        return apiRequest {
            commentAPI.deleteComment(RetrofitService.token!!,id)
        }
    }

    suspend fun updateComment(comment:FutsalComment):FutsalCommentResponse
    {
        return apiRequest {
            commentAPI.updateComment(RetrofitService.token!!,comment)
        }
    }

}