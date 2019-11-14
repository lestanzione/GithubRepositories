package com.stanzione.githubrepositories.di

import com.stanzione.githubrepositories.api.GithubApi
import com.stanzione.githubrepositories.repo.RepoMapper
import com.stanzione.githubrepositories.repo.RepoModule
import com.stanzione.githubrepositories.repo.RepoRepository
import dagger.Component
import javax.inject.Named

import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidModule::class, NetModule::class, RepoModule::class])
interface ApplicationComponent {
    @Named("server")
    fun repoRepository(): RepoRepository
    @Named("local")
    fun localRepoRepository(): RepoRepository
    fun repoMapper(): RepoMapper
    fun api(): GithubApi
}
