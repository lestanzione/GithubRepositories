package com.stanzione.githubrepositories.repo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RepoViewModelFactory(private val repoRepository: RepoRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RepoViewModel(repoRepository) as T
    }
}