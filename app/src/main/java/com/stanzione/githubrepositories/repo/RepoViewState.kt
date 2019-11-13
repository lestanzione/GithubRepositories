package com.stanzione.githubrepositories.repo

import androidx.annotation.StringRes
import com.stanzione.githubrepositories.model.domain.RepoDomain

data class RepoViewState(
    val isLoading: Boolean = true,
    @StringRes val errorMessage: Int? = null,
    val repoList: List<RepoDomain>? = null
)