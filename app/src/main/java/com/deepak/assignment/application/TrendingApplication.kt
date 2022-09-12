package com.deepak.assignment.application

import android.app.Application
import com.deepak.assignment.api.ApiInterface
import com.deepak.assignment.db.TrendingRepoDatabase
import com.deepak.assignment.repository.RepositoryClass
import com.deepak.assignment.retrofit.RetrofitHelper

class TrendingApplication : Application() {

    lateinit var repositoryClass: RepositoryClass

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        val apiInterface : ApiInterface = RetrofitHelper.getInstance().create(ApiInterface::class.java)
        val database = TrendingRepoDatabase.getDatabase(applicationContext)
        repositoryClass = RepositoryClass(apiInterface,database,applicationContext)
    }
}