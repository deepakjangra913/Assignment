package com.deepak.assignment.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deepak.assignment.models.trending_repo.TrendingRepoModel
import com.deepak.assignment.repository.RepositoryClass
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeScreenViewModel(private val repositoryClass: RepositoryClass) : ViewModel() {

    val repositoryList : LiveData<List<TrendingRepoModel.Item>>
        get() = repositoryClass.repoList

    fun getRepositoryList(){
        runBlocking {
            repositoryClass.getTrendingRepos()
        }
    }

}