package com.stanzione.githubrepositories.model

import com.google.gson.annotations.SerializedName

data class Repo(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("forks_count") val forks: Long,
    @SerializedName("stargazers_count") val stars: Long,
    @SerializedName("owner") val owner: Owner
)