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

    override fun getRepositories(page: Int): Single<RepoResponse> {
        return if (isCacheExpired(page) || !hasCache(page)) {
            Single.error(NoCacheError())
        } else {
            Single.just(getCache(page))
        }
    }

    override fun saveRepositories(page: Int, repoResponse: RepoResponse) {
        sharedPrefs.edit().apply {
            putString(SHARED_CACHE_KEY + page, gson.toJson(repoResponse))
            putLong(SHARED_CACHE_TIME_KEY + page, System.currentTimeMillis() + CACHE_TIME)
            apply()
        }
    }

    private fun getCache(page: Int): RepoResponse {
        return gson.fromJson(sharedPrefs.getString(SHARED_CACHE_KEY + page, null), RepoResponse::class.java)
    }

    private fun isCacheExpired(page: Int): Boolean {
        return System.currentTimeMillis() > sharedPrefs.getLong(SHARED_CACHE_TIME_KEY + page, Long.MAX_VALUE)
    }

    private fun hasCache(page: Int): Boolean {
        return sharedPrefs.getString(SHARED_CACHE_KEY + page, null) != null
    }

    companion object {
        private const val CACHE_TIME = 15000 //15 seconds

        private const val SHARED_CACHE_KEY = "cache.repoResponse_"
        private const val SHARED_CACHE_TIME_KEY = "cache.expirationTime_"
    }

}