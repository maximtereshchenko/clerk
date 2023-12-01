package com.github.maximtereshchenko.gateway;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "clerk-read", url = "${clerk.read.url}")
interface ClerkReadClient extends ClerkReadApi {}
