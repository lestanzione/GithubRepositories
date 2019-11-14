package com.stanzione.githubrepositories.repo

import android.content.SharedPreferences
import com.google.gson.Gson
import com.stanzione.githubrepositories.error.NoCacheError
import com.stanzione.githubrepositories.model.RepoResponse
import io.reactivex.Single

class LocalRepoRepository(
    private val sharedPrefs: SharedPreferences,
    private val gson: Gson
) : RepoRepository {

    override fun getRepositories(): Single<RepoResponse> {
        return if (isCacheExpired() || !hasCache()) {
            Single.error(NoCacheError())
        } else {
            Single.just(getCache())
        }
    }

    override fun saveRepositories(repoResponse: RepoResponse) {
        sharedPrefs.edit().apply {
            putString(SHARED_CACHE_KEY, gson.toJson(repoResponse))
            putLong(SHARED_CACHE_TIME_KEY, System.currentTimeMillis() + CACHE_TIME)
            apply()
        }
    }

    private fun getCache(): RepoResponse {
        return gson.fromJson(sharedPrefs.getString(SHARED_CACHE_KEY, null), RepoResponse::class.java)
    }

    private fun isCacheExpired(): Boolean {
        return System.currentTimeMillis() > sharedPrefs.getLong(SHARED_CACHE_TIME_KEY, Long.MAX_VALUE)
    }

    private fun hasCache(): Boolean {
        return sharedPrefs.getString(SHARED_CACHE_KEY, null) != null
    }

    companion object {
        private const val CACHE_TIME = 15000 //15 seconds

        private const val SHARED_CACHE_KEY = "cache.repoResponse"
        private const val SHARED_CACHE_TIME_KEY = "cache.expirationTime"
    }

}