package com.deepak.assignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.deepak.assignment.R
import com.deepak.assignment.databinding.ItemLayoutTrendingRepoBinding
import com.deepak.assignment.models.trending_repo.TrendingRepoModel

//This adapter is used to reflect the data on Recyclerview of Trending Repos
class TrendingRepoAdapter(private val context: Context,val list: MutableList<TrendingRepoModel.Item>) : RecyclerView.Adapter<TrendingRepoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
        ): ViewHolder {
            val view : ItemLayoutTrendingRepoBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.item_layout_trending_repo,parent,false)
            return ViewHolder(view)
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        if (data.isExpand) {
            holder.view.clViewExpand.visibility = View.VISIBLE
        }else{
            holder.view.clViewExpand.visibility = View.GONE
        }

        data.owner.let {
            Glide.with(context).load(data.owner!!.avatar_url)
                .error(ContextCompat.getDrawable(context,R.mipmap.ic_launcher_round))
                .placeholder(ContextCompat.getDrawable(context,R.mipmap.ic_launcher_round))
                .into(holder.view.imvUser)
        }
        data.name.let {
            holder.view.tvUserName.text = data.name
        }
        data.full_name.let {
            holder.view.tvRepositoryName.text = data.full_name
        }
        if (!data.description.isNullOrEmpty()){
            holder.view.tvDescription.text = data.description
            holder.view.tvDescription.visibility = View.VISIBLE
        }
        holder.view.tvForkCount.text =  data.forks_count.toString()
        holder.view.tvStarCount.text = data.stargazers_count.toString()

        if (!data.language.isNullOrEmpty()) {
            holder.view.imvRedCircle.visibility = View.VISIBLE
            holder.view.tvProgrammingLanguage.visibility = View.VISIBLE
            holder.view.tvProgrammingLanguage.text = data.language
        }

        holder.view.clMain.setOnClickListener {
            val expand = data.isExpand
            if (expand){
                data.isExpand = false
                holder.view.clViewExpand.visibility = View.GONE
            }else{
                data.isExpand = true
                holder.view.clViewExpand.visibility = View.VISIBLE
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val view: ItemLayoutTrendingRepoBinding) : RecyclerView.ViewHolder(view.root) {

    }
}