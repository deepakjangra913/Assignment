package com.deepak.assignment.api

import com.deepak.assignment.models.trending_repo.TrendingRepoModel
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("search/repositories?q=stars")
    suspend fun getTrendingRepositories() : Response<TrendingRepoModel>

}