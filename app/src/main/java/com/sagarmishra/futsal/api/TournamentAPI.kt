package com.sagarmishra.futsal.api

import com.sagarmishra.futsal.response.TournamentMatchResponse
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
}
