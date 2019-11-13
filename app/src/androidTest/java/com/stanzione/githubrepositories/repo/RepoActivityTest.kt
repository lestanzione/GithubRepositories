package com.stanzione.githubrepositories.repo

import android.content.Intent
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.stanzione.githubrepositories.GithubRepoApp
import com.stanzione.githubrepositories.R
import com.stanzione.githubrepositories.di.DaggerApplicationComponent
import com.stanzione.githubrepositories.di.MockNetModule
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import okio.Okio
import androidx.test.InstrumentationRegistry
import com.stanzione.githubrepositories.RecyclerViewItemCountAssertion

class RepoActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule<RepoActivity>(RepoActivity::class.java, true, false)

    private val server = MockWebServer()

    @Before
    fun setUp() {
        setUpServer()
        server.start()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    private fun setUpServer() {
        val applicationComponent = DaggerApplicationComponent.builder()
            .netModule(MockNetModule(server))
            .build()

        (getTargetContext().applicationContext as GithubRepoApp)
            .setApplicationComponent(applicationComponent)
    }

    @Test
    fun givenGeneralError_WhenGetRepositories_ShowGeneralErrorMessage() {

        println("server: $server")
        server.enqueue(
            MockResponse()
                .setResponseCode(500)
        )

        activityRule.launchActivity(Intent())

        Thread.sleep(1000)

        onView(allOf(withId(com.google.android.material.R.id.snackbar_text), withText(R.string.message_general_error)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun givenUnprocessableEntityError_WhenGetRepositories_ShowNoMoreDataErrorMessage() {

        println("server: $server")
        server.enqueue(
            MockResponse()
                .setResponseCode(422)
        )

        activityRule.launchActivity(Intent())

        Thread.sleep(1000)

        onView(allOf(withId(com.google.android.material.R.id.snackbar_text), withText(R.string.message_no_more_data)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun givenNetworkError_WhenGetRepositories_ShowNetworkErrorMessage() {

        println("server: $server")
        server.shutdown()

        activityRule.launchActivity(Intent())

        onView(allOf(withId(com.google.android.material.R.id.snackbar_text), withText(R.string.message_network_error)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun givenResponseOk_WhenGetRepositories_ShowRepoList() {

        println("server: $server")
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(readFile("mock_repo_result.json"))
        )

        activityRule.launchActivity(Intent())

        onView(withId(R.id.repo_recycler_view))
            .check(RecyclerViewItemCountAssertion(3))
    }

    private fun readFile(fileName: String): String {
        val stream = InstrumentationRegistry.getContext()
            .assets
            .open(fileName)

        val source = Okio.source(stream)
        val buffer = Okio.buffer(source)

        return buffer.readUtf8()
    }

}