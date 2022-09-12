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

/* This activity is the main Activity
that shows the UI to the user */

class HomeScreenActivity : AppCompatActivity() {

    //Declaration of variables
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

        //create binding object to bind the views with activity
        binding = DataBindingUtil.setContentView(context, R.layout.activity_main)

        //To handle the views of Error Layout
        errorLayoutBinding = binding.viewError

        //Initialize Adapter and set on Recyclerview
        adapter = TrendingRepoAdapter(context, list)
        binding.rvTrendingRepo.adapter = adapter


        val repositoryClass = (application as TrendingApplication).repositoryClass

        viewModel = ViewModelProvider(
            this,
            HomeViewModelFactory(repositoryClass)
        )[HomeScreenViewModel::class.java]

        //Shimmer effect visible
        binding.shimmer.visibility = View.VISIBLE
        binding.shimmer.startShimmerAnimation()

        //Api call
        //To show shimmer effect because api response is very fast
        Handler().postDelayed(Runnable {
            viewModel.getRepositoryList(false)
        },1000)


        //Observe the changes of Data
        viewModel.repositoryList.observe(this) {
            list.clear()
            binding.shimmer.stopShimmerAnimation()
            binding.shimmer.visibility = View.GONE

            // if error is not Null then show error layout
            if (it.getError() != null) {
                binding.tvNoData.visibility = View.GONE
                errorLayoutBinding.viewError.visibility = View.VISIBLE
                binding.rvTrendingRepo.visibility = View.GONE
                binding.swipeRefresh.isEnabled = false
            } else {

                //Response case
                binding.swipeRefresh.isEnabled = true
                if (binding.swipeRefresh.isRefreshing) binding.swipeRefresh.isRefreshing = false

                if(it.getTrendingRepos().isNullOrEmpty()){
                    binding.tvNoData.visibility = View.VISIBLE
                    binding.rvTrendingRepo.visibility = View.GONE
                }else{
                    binding.rvTrendingRepo.visibility = View.VISIBLE
                    binding.tvNoData.visibility = View.GONE
                    list.addAll(it.getTrendingRepos() as MutableList<TrendingRepoModel.Item>)
                    adapter.notifyDataSetChanged()
                }

            }
        }

        //Click on Retry
        errorLayoutBinding.btnRetry.setOnClickListener {
            errorLayoutBinding.viewError.visibility = View.GONE
            binding.shimmer.visibility = View.VISIBLE
            binding.shimmer.startShimmerAnimation()

            //To show shimmer effect because api response is very fast
            Handler().postDelayed(Runnable {
                viewModel.getRepositoryList(false)
            },1000)

        }


        //Pull to refresh
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getRepositoryList(true)
        }
    }
}