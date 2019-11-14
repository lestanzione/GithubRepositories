package com.stanzione.githubrepositories.repo

import com.stanzione.githubrepositories.model.RepoResponse
import io.reactivex.Single

interface RepoRepository {
    fun getRepositories() : Single<RepoResponse>
    fun saveRepositories(repoResponse: RepoResponse)
}