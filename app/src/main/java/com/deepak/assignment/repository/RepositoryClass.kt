package com.deepak.assignment.repository

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.deepak.assignment.api.ApiInterface
import com.deepak.assignment.db.TrendingRepoDatabase
import com.deepak.assignment.models.trending_repo.TrendingRepoModel


class RepositoryClass(
    private val apiInterface: ApiInterface,
    private val trendingRepoDatabase: TrendingRepoDatabase,
    private val context: Context
) {

    private var trendingRepo = MutableLiveData<List<TrendingRepoModel.Item>>()

    val repoList : LiveData<List<TrendingRepoModel.Item>>
    get() = trendingRepo

    suspend fun getTrendingRepos(){
        val dao =  trendingRepoDatabase.trendingDao()
        val databaseValues = dao.getTrendingRepos()

        if (isNetworkAvailable(context)){
            if (databaseValues.isEmpty()) {
                val result = apiInterface.getTrendingRepositories()
                if (result.isSuccessful) {
                    val body = result.body()
                    body.let {
                        if (body!!.items != null) {
                            val data: List<TrendingRepoModel.Item?>? = body.items
                            trendingRepoDatabase.trendingDao()
                                .addTrendingRepos(data as List<TrendingRepoModel.Item>)
                            trendingRepo.value = data
                        }
                    }
                }else{
                    trendingRepo.value = null
                }
            }else{
                trendingRepo.value = databaseValues
            }
        }else{
            if (databaseValues.isEmpty()){
                trendingRepo.value = dao.getTrendingRepos()
            }else{
                trendingRepo.value = null
            }

        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!
            .isConnected
    }

    

}