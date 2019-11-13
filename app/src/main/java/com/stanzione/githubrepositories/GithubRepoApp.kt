package com.stanzione.githubrepositories

import android.app.Application
import androidx.annotation.VisibleForTesting
import com.stanzione.githubrepositories.di.ApplicationComponent
import com.stanzione.githubrepositories.di.DaggerApplicationComponent
import com.stanzione.githubrepositories.di.NetModule

class GithubRepoApp : Application() {

    private lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        applicationComponent = DaggerApplicationComponent.builder()
            .netModule(NetModule())
            .build()
    }

    fun getApplicationComponent(): ApplicationComponent {
        return applicationComponent
    }

    @VisibleForTesting
    fun setApplicationComponent(applicationComponent: ApplicationComponent) {
        this.applicationComponent = applicationComponent
    }

    companion object {
        private var INSTANCE: GithubRepoApp? = null

        @JvmStatic
        fun get(): GithubRepoApp = INSTANCE!!
    }

}