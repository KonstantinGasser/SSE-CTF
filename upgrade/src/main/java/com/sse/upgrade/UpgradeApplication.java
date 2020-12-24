package com.sse.upgrade;

import com.sse.upgrade.security.PasswordAuthenticationProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UpgradeApplication {

    public static void main(String[] args) {
        SpringApplication.run(UpgradeApplication.class, args);
    }

    @Bean
    PasswordAuthenticationProvider createPasswordAuthenticationProvider() {
        return new PasswordAuthenticationProvider();
    }
}
