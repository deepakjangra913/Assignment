package com.deepak.assignment.worker
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.deepak.assignment.application.TrendingApplication
import kotlinx.coroutines.runBlocking

//This class is used to run a periodic task after every two hours
class MyWorker(context: Context, private val workerParams: WorkerParameters) : Worker(context,workerParams){

    override fun doWork(): Result {

       val context =  applicationContext as TrendingApplication
        runBlocking {
            context.dao.deleteAllFromTable()
        }
        return Result.success()
    }


}