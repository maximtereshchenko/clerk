package com.github.maximtereshchenko.gateway;

import com.github.maximtereshchenko.outbox.Outbox;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Clock;

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableFeignClients(basePackageClasses = ClerkReadClient.class)
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class);
    }

    @Bean
    ClerkReadController clerkReadController(ClerkReadClient clerkReadClient) {
        return new ClerkReadController(clerkReadClient, new TranslatorImpl());
    }

    @Bean
    ClerkWriteController clerkWriteController(Outbox outbox, @Value("${clerk.write.topic}") String topic) {
        return new ClerkWriteController(outbox, topic);
    }

    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(customizer ->
                        customizer.requestMatchers("/actuator/**").permitAll()
                                .anyRequest().authenticated()

                )
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer.jwt(Customizer.withDefaults())
                )
                .build();
    }
}
