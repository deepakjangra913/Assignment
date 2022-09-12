package com.deepak.assignment.models.trending_repo
import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Keep
data class TrendingRepoModel(
    var incomplete_results: Boolean?,
    var items: List<Item?>?,
    var total_count: Int?
) {

    @Entity(tableName = "trendingRepo")
    @Keep
    data class Item(
        var isExpand: Boolean = false,
        var description: String?,
        var forks_count: Int?,
        var full_name: String?,
        @PrimaryKey(autoGenerate = false)
        var id: Int?,
        var language: String?,
        var name: String?,
        @Embedded
        var owner: Owner?,
        var stargazers_count: Int?,
    ) {
        @Entity
        @Keep
        data class Owner(
            var avatar_url: String?
        )
    }
}