package com.stanzione.githubrepositories.repo.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import com.stanzione.githubrepositories.R
import com.stanzione.githubrepositories.model.domain.RepoDomain

class RepoAdapter(private val repoList: MutableList<RepoDomain>) : RecyclerView.Adapter<RepoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_repo, parent, false)
        return RepoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return repoList.size
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bindData(repoList[position])
    }

    fun addItems(repoList: List<RepoDomain>) {
        this.repoList.addAll(repoList)
        notifyDataSetChanged()
    }
}