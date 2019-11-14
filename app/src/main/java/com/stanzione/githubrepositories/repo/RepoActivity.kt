package com.stanzione.githubrepositories.repo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.stanzione.githubrepositories.R
import com.stanzione.githubrepositories.di.Injector
import com.stanzione.githubrepositories.repo.adapter.RepoAdapter
import kotlinx.android.synthetic.main.activity_main.*

class RepoActivity : AppCompatActivity() {

    private lateinit var repoViewModelFactory: RepoViewModelFactory
    private lateinit var repoViewModel: RepoViewModel
    private lateinit var repoAdapter: RepoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupView()
        setupViewModel()
        setupObservers()

        repoViewModel.getRepositories()
    }

    private fun setupView() {

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Repositories"

        repoAdapter = RepoAdapter(mutableListOf())
        repo_recycler_view.layoutManager = LinearLayoutManager(applicationContext)
        repo_recycler_view.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))
        repo_recycler_view.adapter = repoAdapter
    }

    private fun setupViewModel() {
        repoViewModelFactory = RepoViewModelFactory(
            Injector.get().repoRepository(),
            Injector.get().localRepoRepository(),
            Injector.get().repoMapper()
        )
        repoViewModel = ViewModelProviders.of(this, repoViewModelFactory).get(RepoViewModel::class.java)
    }

    private fun setupObservers() {
        repoViewModel.viewState.observe(this, Observer {

            when (it.isLoading) {
                true -> progress_bar.visibility = View.VISIBLE
                false -> progress_bar.visibility = View.GONE
            }

            it.errorMessage?.let { message ->
                Snackbar.make(coordinator_layout, message, Snackbar.LENGTH_SHORT).show()
            }

            it.repoList?.let { repoList ->
                repoAdapter.addItems(repoList)
            }
        })
    }
}
