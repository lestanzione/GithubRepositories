package com.stanzione.githubrepositories.repo

import androidx.annotation.StringRes

data class RepoViewState(
    val isLoading: Boolean = true,
    @StringRes val errorMessage: Int? = null
)