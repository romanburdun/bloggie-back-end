package com.bloggie.server.fixtures;

import com.bloggie.server.security.principals.UserPrincipal;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;

@TestConfiguration
public class AuthTestConfig {

        @Bean
        @Primary
        public UserDetailsService userDetailsService() {

            UserDetails writer = UserPrincipal.create( UsersFixtures.getWriterUser());
            UserDetails admin = UserPrincipal.create( UsersFixtures.getAdminUser());
            return new InMemoryUserDetailsManager(Arrays.asList(writer,admin));
        }
}
