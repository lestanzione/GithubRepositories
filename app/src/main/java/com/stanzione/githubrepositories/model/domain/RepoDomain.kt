package com.stanzione.githubrepositories.model.domain

data class RepoDomain(
    val id: Long,
    val name: String,
    val forks: String,
    val stars: String,
    val ownerName: String,
    val avatarUrl: String
)