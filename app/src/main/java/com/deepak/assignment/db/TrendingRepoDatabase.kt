package com.deepak.assignment.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.deepak.assignment.models.trending_repo.TrendingRepoModel

/* This class is used to get the instance of Dao */
@Database(entities = [TrendingRepoModel.Item::class], version = 1)
abstract class TrendingRepoDatabase : RoomDatabase() {

    abstract fun trendingDao() : TrendingDao

    companion object{
        @Volatile
        private var INSTANCE : TrendingRepoDatabase? = null

        fun getDatabase(context: Context) : TrendingRepoDatabase{
            if (INSTANCE ==null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                    TrendingRepoDatabase::class.java,
                    "trendingRepoDB")
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}