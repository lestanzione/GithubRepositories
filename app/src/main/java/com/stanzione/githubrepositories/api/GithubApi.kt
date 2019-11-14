package com.stanzione.githubrepositories.api

import com.stanzione.githubrepositories.model.RepoResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {

    @GET("search/repositories?q=language:kotlin&sort=stars")
    fun getRepositories(@Query("page") page: Int): Single<RepoResponse>

}