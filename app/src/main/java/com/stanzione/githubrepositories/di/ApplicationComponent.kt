package com.stanzione.githubrepositories.di

import com.stanzione.githubrepositories.api.GithubApi
import com.stanzione.githubrepositories.repo.RepoActivity
import com.stanzione.githubrepositories.repo.RepoMapper
import com.stanzione.githubrepositories.repo.RepoModule
import com.stanzione.githubrepositories.repo.RepoRepository
import dagger.Component
import retrofit2.Retrofit

import javax.inject.Singleton

@Singleton
@Component(modules = [NetModule::class, RepoModule::class])
interface ApplicationComponent {
    fun repoRepository(): RepoRepository
    fun repoMapper(): RepoMapper
    fun api(): GithubApi
}
