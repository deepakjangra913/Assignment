package com.deepak.assignment.views

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.deepak.assignment.R
import com.deepak.assignment.adapter.TrendingRepoAdapter
import com.deepak.assignment.application.TrendingApplication
import com.deepak.assignment.databinding.ActivityMainBinding
import com.deepak.assignment.databinding.LayoutApiErrorBinding
import com.deepak.assignment.models.trending_repo.TrendingRepoModel
import com.deepak.assignment.view_model.HomeScreenViewModel
import com.deepak.assignment.view_model.HomeViewModelFactory

class HomeScreenActivity : AppCompatActivity() {

    private lateinit var viewModel: HomeScreenViewModel
    private val context: Activity = this
    private lateinit var binding: ActivityMainBinding
    private lateinit var errorLayoutBinding: LayoutApiErrorBinding
    private val TAG = javaClass.simpleName
    private lateinit var adapter: TrendingRepoAdapter
    private var list: MutableList<TrendingRepoModel.Item> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(context, R.layout.activity_main)
        errorLayoutBinding = binding.viewError

        adapter = TrendingRepoAdapter(context, list)
        binding.rvTrendingRepo.adapter = adapter


        val repositoryClass = (application as TrendingApplication).repositoryClass

        viewModel = ViewModelProvider(
            this,
            HomeViewModelFactory(repositoryClass)
        )[HomeScreenViewModel::class.java]

        //Api call
        binding.shimmer.visibility = View.VISIBLE
        binding.shimmer.startShimmerAnimation()

        Handler().postDelayed(Runnable {
            viewModel.getRepositoryList()
        },2000)

        viewModel.repositoryList.observe(this) {
            Log.e(TAG, " response: $it")
            binding.shimmer.stopShimmerAnimation()
            binding.shimmer.visibility = View.GONE
            if (it.isNullOrEmpty()) {
                errorLayoutBinding.viewError.visibility = View.VISIBLE
                binding.rvTrendingRepo.visibility = View.GONE
                binding.swipeRefresh.isEnabled = false
            } else {
                binding.swipeRefresh.isEnabled = true
                if (binding.swipeRefresh.isRefreshing) binding.swipeRefresh.isRefreshing = false
                binding.rvTrendingRepo.visibility = View.VISIBLE
                list.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }

        errorLayoutBinding.btnRetry.setOnClickListener {
            errorLayoutBinding.viewError.visibility = View.GONE
            binding.shimmer.visibility = View.VISIBLE
            binding.shimmer.startShimmerAnimation()
            Handler().postDelayed(Runnable {
                viewModel.getRepositoryList()
            },2000)
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getRepositoryList()
        }
    }
}