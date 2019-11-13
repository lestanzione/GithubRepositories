package com.stanzione.githubrepositories.repo

import com.stanzione.githubrepositories.api.GithubApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepoModule {

    @Provides
    @Singleton
    fun getRepoRepository(api: GithubApi): RepoRepository {
        return GithubRepoRepository(api)
    }

}
