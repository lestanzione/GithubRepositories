package com.stanzione.githubrepositories.repo

import com.stanzione.githubrepositories.model.RepoResponse
import io.reactivex.Single
import com.stanzione.githubrepositories.api.GithubApi

class GithubRepoRepository(private val api: GithubApi) : RepoRepository {

    override fun getRepositories(): Single<RepoResponse> {
        return api.getRepositories()
    }

}