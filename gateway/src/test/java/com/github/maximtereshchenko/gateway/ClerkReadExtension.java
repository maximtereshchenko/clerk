package com.github.maximtereshchenko.gateway;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;

final class ClerkReadExtension extends WireMockExtension {

    ClerkReadExtension() {
        super(newInstance());
    }

    @Override
    protected void onBeforeAll(WireMockRuntimeInfo wireMockRuntimeInfo) {
        System.setProperty("clerk.read.url", wireMockRuntimeInfo.getHttpBaseUrl());
    }
}
