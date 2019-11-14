package com.stanzione.githubrepositories.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.stanzione.githubrepositories.R
import com.stanzione.githubrepositories.error.NoCacheError
import com.stanzione.githubrepositories.model.Owner
import com.stanzione.githubrepositories.model.Repo
import com.stanzione.githubrepositories.model.RepoResponse
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

    @Mock
    lateinit var mockLocalRepository: RepoRepository

    @Mock
    lateinit var mockRepoMapper: RepoMapper

    private lateinit var viewModel: RepoViewModel

    private var repoResponse: RepoResponse
    private var page = 1

    init {
        val repoList = mutableListOf<Repo>()
        repoList.add(
            Repo(
                1,
                "Name",
                2,
                3,
                Owner(
                    1,
                    "Owner",
                    "url"
                )
            )
        )

        repoResponse = RepoResponse(repoList)
    }

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
        viewModel = RepoViewModel(mockRepoRepository, mockLocalRepository, mockRepoMapper)
    }

    @Test
    fun `given no cache and model with network error, when get repositories, show network error message`() {
        `when`(mockLocalRepository.getRepositories(page)).thenReturn(Single.error(NoCacheError()))
        `when`(mockRepoRepository.getRepositories(page)).thenReturn(Single.error(IOException()))

        viewModel.getRepositories(page)

        assertEquals(false, viewModel.viewState.value?.isLoading)
        assertEquals(R.string.message_network_error, viewModel.viewState.value?.errorMessage)
        assertNull(viewModel.viewState.value?.repoList)

        verify(mockRepoRepository, times(1)).getRepositories(page)
        verify(mockLocalRepository, times(1)).getRepositories(page)
        verifyNoMoreInteractions(mockRepoRepository, mockLocalRepository, mockRepoMapper)
    }

    @Test
    fun `given no cache and model with general error, when get repositories, show general error message`() {
        `when`(mockLocalRepository.getRepositories(page)).thenReturn(Single.error(NoCacheError()))
        `when`(mockRepoRepository.getRepositories(page)).thenReturn(Single.error(Exception()))

        viewModel.getRepositories(page)

        assertEquals(false, viewModel.viewState.value?.isLoading)
        assertEquals(R.string.message_general_error, viewModel.viewState.value?.errorMessage)
        assertNull(viewModel.viewState.value?.repoList)

        verify(mockRepoRepository, times(1)).getRepositories(page)
        verify(mockLocalRepository, times(1)).getRepositories(page)
        verifyNoMoreInteractions(mockRepoRepository, mockLocalRepository, mockRepoMapper)
    }

    @Test
    fun `given no cache and model with repo list and map done, when get repositories, show repo list`() {
        `when`(mockLocalRepository.getRepositories(page)).thenReturn(Single.error(NoCacheError()))
        `when`(mockRepoRepository.getRepositories(page)).thenReturn(Single.just(repoResponse))
        `when`(mockRepoMapper.transform(repoResponse.repoList)).thenReturn(listOf())

        viewModel.getRepositories(page)

        assertEquals(false, viewModel.viewState.value?.isLoading)
        assertNull(viewModel.viewState.value?.errorMessage)
        assertNotNull(viewModel.viewState.value?.repoList)

        verify(mockRepoRepository, times(1)).getRepositories(page)
        verify(mockLocalRepository, times(1)).getRepositories(page)
        verify(mockLocalRepository, times(1)).saveRepositories(page, repoResponse)
        verify(mockRepoMapper, times(1)).transform(repoResponse.repoList)
        verifyNoMoreInteractions(mockRepoRepository, mockLocalRepository, mockRepoMapper)
    }

    @Test
    fun `given with cache and map done, when get repositories, show repo list`() {
        `when`(mockLocalRepository.getRepositories(page)).thenReturn(Single.just(repoResponse))
        `when`(mockRepoMapper.transform(repoResponse.repoList)).thenReturn(listOf())

        viewModel.getRepositories(page)

        assertEquals(false, viewModel.viewState.value?.isLoading)
        assertNull(viewModel.viewState.value?.errorMessage)
        assertNotNull(viewModel.viewState.value?.repoList)

        verify(mockLocalRepository, times(1)).getRepositories(page)
        verify(mockRepoMapper, times(1)).transform(repoResponse.repoList)
        verifyNoMoreInteractions(mockRepoRepository, mockLocalRepository, mockRepoMapper)
    }

}