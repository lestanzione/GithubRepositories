package com.stanzione.githubrepositories.repo

import android.content.SharedPreferences
import com.google.gson.Gson
import com.stanzione.githubrepositories.api.GithubApi
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class RepoModule {

    @Provides
    @Named("server")
    @Singleton
    fun getRepoRepository(api: GithubApi): RepoRepository {
        return GithubRepoRepository(api)
    }

    @Provides
    @Named("local")
    @Singleton
    fun getLocalRepoRepository(sharedPreferences: SharedPreferences, gson: Gson): RepoRepository {
        return LocalRepoRepository(sharedPreferences, gson)
    }

    @Provides
    @Singleton
    fun getRepoMapper(): RepoMapper {
        return RepoMapper()
    }

}
