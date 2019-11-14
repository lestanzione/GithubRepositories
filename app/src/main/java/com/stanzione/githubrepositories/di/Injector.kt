package com.stanzione.githubrepositories.di

import com.stanzione.githubrepositories.GithubRepoApp

class Injector private constructor() {
    companion object {
        fun get(): ApplicationComponent = GithubRepoApp.get().getApplicationComponent()
    }
}