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
import androidx.recyclerview.widget.RecyclerView

class RepoActivity : AppCompatActivity() {

    private lateinit var repoViewModelFactory: RepoViewModelFactory
    private lateinit var repoViewModel: RepoViewModel

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var repoAdapter: RepoAdapter

    private var previousTotal = 0
    private var loading = true
    private var visibleThreshold = 5
    private var firstVisibleItem: Int = 0
    private var visibleItemCount:Int = 0
    private var totalItemCount:Int = 0
    private var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupView()
        setupViewModel()
        setupObservers()

        if (savedInstanceState == null) {
            repoViewModel.getRepositories(currentPage)
        }
    }

    private fun setupView() {

        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.title_repositories)

        layoutManager = LinearLayoutManager(applicationContext)
        repoAdapter = RepoAdapter(mutableListOf())
        repo_recycler_view.layoutManager = layoutManager
        repo_recycler_view.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))
        repo_recycler_view.adapter = repoAdapter


        repo_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                visibleItemCount = repo_recycler_view.childCount
                totalItemCount = layoutManager.itemCount
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false
                        previousTotal = totalItemCount
                    }
                }
                if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
                    loading = true
                    repoViewModel.getRepositories(currentPage)
                }
            }
        })
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
                Snackbar.make(coordinator_layout, message, Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.message_try_again), ( {
                        repoViewModel.getRepositories(currentPage)
                    } ))
                    .show()
            }

            it.repoList?.let { repoList ->
                repoAdapter.clearItems()
                repoAdapter.addItems(repoList)
                currentPage++
            }
        })
    }
}
