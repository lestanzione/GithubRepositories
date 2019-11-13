package com.stanzione.githubrepositories.api

import com.stanzione.githubrepositories.model.RepoResponse
import io.reactivex.Single
import retrofit2.http.GET

interface GithubApi {

    @GET("search/repositories?q=language:kotlin&sort=stars&page=1")
    fun getRepositories(): Single<RepoResponse>

}