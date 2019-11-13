package com.stanzione.githubrepositories.repo.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.stanzione.githubrepositories.R
import com.stanzione.githubrepositories.model.domain.RepoDomain
import kotlinx.android.synthetic.main.item_repo.view.*

class RepoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindData(repo: RepoDomain) {
        itemView.item_repo_name.text = repo.name
        itemView.item_repo_forks.text = repo.forks
        itemView.item_repo_stars.text = repo.stars
        itemView.item_repo_owner_name.text = repo.ownerName

        Picasso.with(itemView.context)
            .load(repo.avatarUrl)
            .fit()
            .centerCrop()
            .placeholder(R.drawable.placeholder_owner_image)
            .into(itemView.item_repo_owner_image)
    }

}