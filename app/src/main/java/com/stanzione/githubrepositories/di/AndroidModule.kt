package com.stanzione.githubrepositories.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AndroidModule(private val context: Context) {

    @Provides
    @Singleton
    fun getGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun getSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences("Repositories", Context.MODE_PRIVATE)
    }

}