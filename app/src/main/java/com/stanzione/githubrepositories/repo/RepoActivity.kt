package com.stanzione.githubrepositories.repo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.stanzione.githubrepositories.R
import com.stanzione.githubrepositories.di.Injector
import kotlinx.android.synthetic.main.activity_main.*

class RepoActivity : AppCompatActivity() {

    private lateinit var repoViewModelFactory: RepoViewModelFactory
    private lateinit var repoViewModel: RepoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()
        setupObservers()

        repoViewModel.getRepositories()
    }

    private fun setupViewModel() {
        repoViewModelFactory = RepoViewModelFactory(Injector.get().repoRepository())
        repoViewModel = ViewModelProviders.of(this, repoViewModelFactory).get(RepoViewModel::class.java)
    }

    private fun setupObservers() {
        repoViewModel.viewState.observe(this, Observer {

            when (it.isLoading) {
                true -> progress_bar.visibility = View.VISIBLE
                false -> progress_bar.visibility = View.GONE
            }

            it.errorMessage?.let { message ->
                Snackbar.make(constraint_layout, message, Snackbar.LENGTH_SHORT).show()
            }
        })
    }
}
