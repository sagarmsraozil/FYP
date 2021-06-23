package com.sagarmishra.futsal.api

import com.sagarmishra.futsal.response.*
import retrofit2.Response
import retrofit2.http.*

interface TournamentAPI {
    @GET("myTeamTournamentHistory/{tid}")
    suspend fun getTournamentMatches(
        @Path("tid") tid:String
    ): Response<TournamentMatchResponse>

    @GET("myUpcomingTournamentMatches/{tid}")
    suspend fun upcomingTournamentMatches(
        @Path("tid")tid:String
    ):Response<TournamentMatchResponse>

    @GET("fetchTournaments")
    suspend fun fetchTournaments():Response<TournamentResponse>

    @GET("getTournament/{tid}")
    suspend fun getTournament(
        @Header("Authorization") token:String,
        @Path("tid") tid:String
    ):Response<SingleTournamentResponse>

    @FormUrlEncoded
    @POST("participateInTournament")
    suspend fun paticipateInTournament(
        @Header("Authorization") token:String,
        @Field("tid") tid:String
    ):Response<GlobalResponse>

    @FormUrlEncoded
    @POST("removeParticipation")
    suspend fun removeParticipation(
        @Header("Authorization") token:String,
        @Field("tid") tid:String
    ):Response<GlobalResponse>

    @GET("getUpcomingMatches/{tid}")
    suspend fun tournamentMatches(
        @Path("tid") tid:String
    ):Response<TournamentMatchResponse>

    @GET("teamPerformance/{tournamentId}/{teamId}")
    suspend fun teamPerformance(
        @Path("tournamentId") tid:String,
        @Path("teamId") teamId:String
    ):Response<TournamentPerformanceResponse>
}
