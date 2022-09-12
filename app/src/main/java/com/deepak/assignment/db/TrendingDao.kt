package com.deepak.assignment.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.deepak.assignment.models.trending_repo.TrendingRepoModel

@Dao
interface TrendingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrendingRepos(repos : List<TrendingRepoModel.Item>)

    @Query("SELECT * FROM trendingRepo")
    suspend fun getTrendingRepos() : List<TrendingRepoModel.Item>
}