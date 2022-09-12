package com.deepak.assignment.application

import android.app.Application
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.deepak.assignment.api.ApiInterface
import com.deepak.assignment.db.TrendingDao
import com.deepak.assignment.db.TrendingRepoDatabase
import com.deepak.assignment.repository.RepositoryClass
import com.deepak.assignment.retrofit.RetrofitHelper
import com.deepak.assignment.worker.MyWorker
import java.util.concurrent.TimeUnit


// This is main application responsible for creating a instance of Repository class and dao
class TrendingApplication : Application() {

    lateinit var repositoryClass: RepositoryClass
    lateinit var dao: TrendingDao

    override fun onCreate() {
        super.onCreate()
        initialize()

        //Set constraints for work manager
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        //Initialize periodic task
        PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS
        val periodicWork =
            PeriodicWorkRequest.Builder(MyWorker::class.java, 2, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()
        WorkManager.getInstance().enqueue(periodicWork)
    }

    private fun initialize() {

        //Initialize api interface, database and repository class
        val apiInterface: ApiInterface =
            RetrofitHelper.getInstance().create(ApiInterface::class.java)
        val database = TrendingRepoDatabase.getDatabase(applicationContext)
        dao = database.trendingDao()
        repositoryClass = RepositoryClass(apiInterface, dao, applicationContext)
    }
}