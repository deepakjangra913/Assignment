package com.deepak.assignment.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deepak.assignment.api.ApiResponse
import com.deepak.assignment.models.trending_repo.TrendingRepoModel
import com.deepak.assignment.repository.RepositoryClass
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


//This view model is used to communicate with repository class to get and send data
class HomeScreenViewModel(private val repositoryClass: RepositoryClass) : ViewModel() {

    val repositoryList : LiveData<ApiResponse>
        get() = repositoryClass.repoList

    // Api call
    fun getRepositoryList(fromPullToRefresh : Boolean){

        // Run a coroutine to call suspend function
        runBlocking {
            repositoryClass.getTrendingRepos(fromPullToRefresh)
        }
    }

}