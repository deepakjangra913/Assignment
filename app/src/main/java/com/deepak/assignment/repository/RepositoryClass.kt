package com.deepak.assignment.repository

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.deepak.assignment.api.ApiInterface
import com.deepak.assignment.api.ApiResponse
import com.deepak.assignment.db.TrendingDao
import com.deepak.assignment.models.trending_repo.TrendingRepoModel
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import java.util.logging.Handler

//This repository class is responsible for network request and also return the response and error
class RepositoryClass(
    private val apiInterface: ApiInterface,
    private val dao: TrendingDao,
    private val context: Context
) {

    private var trendingRepo = MutableLiveData<ApiResponse>()

    val repoList : LiveData<ApiResponse>
    get() = trendingRepo

    suspend fun getTrendingRepos(fromPullToRefresh : Boolean) {
        val databaseValues = dao.getTrendingRepos()

        try {
            if (isNetworkAvailable(context)){
                // To forcefully api hit when user use pull to refresh
                if(fromPullToRefresh){
                    val result: Response<TrendingRepoModel> =
                        apiInterface.getTrendingRepositories()
                    if (result.isSuccessful) {
                        val body = result.body()
                        body.let {
                            if (body!!.items != null) {
                                val data: List<TrendingRepoModel.Item?>? = body.items
                                dao.addTrendingRepos(data as List<TrendingRepoModel.Item>)
                                trendingRepo.value = ApiResponse(data)
                            }
                        }
                    } else {
                        trendingRepo.value =
                            ApiResponse(Throwable(result.errorBody().toString()))
                    }
                }else {
                    if (databaseValues.isEmpty()) {
                        val result: Response<TrendingRepoModel> =
                            apiInterface.getTrendingRepositories()
                        if (result.isSuccessful) {
                            val body = result.body()
                            body.let {
                                if (body!!.items != null) {
                                    val data: List<TrendingRepoModel.Item?>? = body.items
                                    dao.addTrendingRepos(data as List<TrendingRepoModel.Item>)
                                    trendingRepo.value = ApiResponse(data)
                                }
                            }
                        } else {
                            trendingRepo.value =
                                ApiResponse(Throwable(result.errorBody().toString()))
                        }
                    } else {
                        trendingRepo.value = ApiResponse(databaseValues)
                    }
                }
            }else{
                if (databaseValues.isNotEmpty()){
                    trendingRepo.value = ApiResponse(dao.getTrendingRepos())
                }else{
                    trendingRepo.value = ApiResponse(Throwable("No Internet"))
                }

            }
        }catch (e : Exception){
            trendingRepo.value = ApiResponse(Throwable(e.localizedMessage))
        }

    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!
            .isConnected
    }

    

}