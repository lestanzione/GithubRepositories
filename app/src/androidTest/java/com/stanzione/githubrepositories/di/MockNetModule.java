package com.stanzione.githubrepositories.di;

import okhttp3.mockwebserver.MockWebServer;

public class MockNetModule extends NetModule {

    private final MockWebServer server;

    public MockNetModule(MockWebServer server) {
        this.server = server;
    }

    @Override
    public String getBaseUrl() {
        return server.url("/").toString();
    }
}
