package com.stanzione.githubrepositories.repo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RepoViewModelFactory(
    private val repoRepository: RepoRepository,
    private val localRepoRepository: RepoRepository,
    private val repoMapper: RepoMapper
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RepoViewModel(repoRepository, localRepoRepository, repoMapper) as T
    }
}