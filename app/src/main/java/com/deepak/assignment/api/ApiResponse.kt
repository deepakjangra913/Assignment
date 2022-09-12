package com.deepak.assignment.api

import com.deepak.assignment.models.trending_repo.TrendingRepoModel

//This api is handle to
class ApiResponse() {

    private var error: Throwable? = null
    private var repos: List<TrendingRepoModel.Item>? = null

    constructor(repos : List<TrendingRepoModel.Item>):this(){
        this.repos = repos
        this.error = null
    }

    constructor(error : Throwable):this(){
        this.repos = null;
        this.error = error;
    }


    fun getError() : Throwable? {
        return error
    }

    fun getTrendingRepos(): List<TrendingRepoModel.Item>? {
        return repos
    }


}