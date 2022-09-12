package com.deepak.assignment.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.deepak.assignment.models.trending_repo.TrendingRepoModel

// This interface is used to store values into data base and get that values from database
@Dao
interface TrendingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrendingRepos(repos : List<TrendingRepoModel.Item>)

    @Query("SELECT * FROM trendingRepo")
    suspend fun getTrendingRepos() : List<TrendingRepoModel.Item>

    @Query("DELETE FROM trendingRepo")
    suspend fun deleteAllFromTable()
}