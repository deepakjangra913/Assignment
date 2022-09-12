package com.deepak.assignment.view_model

import android.widget.ViewSwitcher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.deepak.assignment.repository.RepositoryClass

class HomeViewModelFactory(private val repositoryClass: RepositoryClass) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeScreenViewModel(repositoryClass) as T
    }
}