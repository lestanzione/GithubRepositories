package com.stanzione.githubrepositories.model

import com.google.gson.annotations.SerializedName

data class RepoResponse(
    @SerializedName("items") val repoList: List<Repo>
)