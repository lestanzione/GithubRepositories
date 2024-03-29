package com.stanzione.githubrepositories.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stanzione.githubrepositories.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.stanzione.githubrepositories.error.NoCacheError
import com.stanzione.githubrepositories.model.domain.RepoDomain


class RepoViewModel(
    private val repoRepository: RepoRepository,
    private val localRepoRepository: RepoRepository,
    private val repoMapper: RepoMapper
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val repoList = mutableListOf<RepoDomain>()

    private val _viewState = MutableLiveData<RepoViewState>()
    val viewState: LiveData<RepoViewState>
        get() = _viewState

    init {
        _viewState.value = RepoViewState()
    }

    private fun currentViewState(): RepoViewState = _viewState.value!!

    fun getRepositories(page: Int) {

        compositeDisposable.add(
            localRepoRepository.getRepositories(page).
                onErrorResumeNext {
                    if (it is NoCacheError) {
                        repoRepository.getRepositories(page)
                            .map { repoResponse ->
                                localRepoRepository.saveRepositories(page, repoResponse)
                                repoResponse
                            }
                    } else {
                        throw it
                    }
                }
                .map {
                    return@map repoMapper.transform(it.repoList)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { onRepoDomainListReceived(it) },
                    { onRepoResponseError(it) }
                )
        )

    }

    private fun onRepoDomainListReceived(repoDomainList: List<RepoDomain>) {
        repoList.addAll(repoDomainList)
        _viewState.value = currentViewState().copy(isLoading = false, errorMessage = null, repoList = repoList)
    }

    private fun onRepoResponseError(throwable: Throwable) {
        println("Error: $throwable")
        val messageRes = when (throwable) {
            is IOException -> R.string.message_network_error
            else -> {
                if ((throwable as? HttpException)?.code() == 422) {
                    R.string.message_no_more_data
                } else {
                    R.string.message_general_error
                }
            }
        }

        _viewState.value = currentViewState().copy(isLoading = false, errorMessage = messageRes, repoList = null)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}