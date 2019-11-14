package com.stanzione.githubrepositories.repo

import com.stanzione.githubrepositories.model.RepoResponse
import io.reactivex.Single

interface RepoRepository {
    fun getRepositories(page: Int) : Single<RepoResponse>
    fun saveRepositories(page: Int, repoResponse: RepoResponse)
}