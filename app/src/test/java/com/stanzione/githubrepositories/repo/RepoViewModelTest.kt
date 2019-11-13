package com.stanzione.githubrepositories.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.stanzione.githubrepositories.R
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.*
import org.junit.Assert.*
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.io.IOException
import java.util.concurrent.TimeUnit

class RepoViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var mockRepoRepository: RepoRepository

    private lateinit var viewModel: RepoViewModel

    companion object {
        @BeforeClass
        @JvmStatic
        fun setupRxSchedulers() {

            val immediate = object : Scheduler() {

                override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
                    return super.scheduleDirect(run, 0, unit)
                }

                override fun createWorker(): Worker {
                    return ExecutorScheduler.ExecutorWorker(Runnable::run)
                }
            }

            RxJavaPlugins.setInitIoSchedulerHandler { immediate }
            RxJavaPlugins.setInitComputationSchedulerHandler { immediate }
            RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
            RxJavaPlugins.setInitSingleSchedulerHandler { immediate }
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }

        }

        @AfterClass
        @JvmStatic
        fun tearDownRxSchedulers() {
            RxJavaPlugins.reset()
            RxAndroidPlugins.reset()
        }

    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = RepoViewModel(mockRepoRepository)
    }

    @Test
    fun `given model with network error, when get repositories, show network error message`() {
        `when`(mockRepoRepository.getRepositories()).thenReturn(Single.error(IOException()))

        viewModel.getRepositories()

        assertEquals(false, viewModel.viewState.value?.isLoading)
        assertEquals(R.string.message_network_error, viewModel.viewState.value?.errorMessage)
        verify(mockRepoRepository, times(1)).getRepositories()
        verifyNoMoreInteractions(mockRepoRepository)
    }

    @Test
    fun `given model with general error, when get repositories, show general error message`() {
        `when`(mockRepoRepository.getRepositories()).thenReturn(Single.error(Exception()))

        viewModel.getRepositories()

        assertEquals(false, viewModel.viewState.value?.isLoading)
        assertEquals(R.string.message_general_error, viewModel.viewState.value?.errorMessage)
        verify(mockRepoRepository, times(1)).getRepositories()
        verifyNoMoreInteractions(mockRepoRepository)
    }

}