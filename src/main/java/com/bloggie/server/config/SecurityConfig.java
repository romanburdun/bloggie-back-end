package com.bloggie.server.config;

import com.bloggie.server.security.jwt.JwtAuthenticationFilter;
import com.bloggie.server.security.jwt.JwtEntryPoint;
import com.bloggie.server.services.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)

@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;
    private JwtEntryPoint jwtEntryPoint;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().formLogin().disable()
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/site").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/posts/excerpts***").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/posts/by-title***").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/posts/by-content***").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/pages/***").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/media/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/posts").hasAnyRole("ADMINISTRATOR", "WRITER")
                .antMatchers(HttpMethod.POST, "/api/v1/media/***").hasAnyRole("ADMINISTRATOR", "WRITER")
                .antMatchers(HttpMethod.GET, "/api/v1/media").hasAnyRole("ADMINISTRATOR", "WRITER")
                .antMatchers(HttpMethod.PUT, "/api/v1/posts/**").hasAnyRole("ADMINISTRATOR", "WRITER")
                .antMatchers(HttpMethod.DELETE, "/api/v1/posts/**").hasAnyRole("ADMINISTRATOR", "WRITER")
                .antMatchers(HttpMethod.GET, "/api/v1/posts").hasAnyRole("ADMINISTRATOR", "WRITER")
                .antMatchers(HttpMethod.POST, "/api/v1/pages").hasRole("ADMINISTRATOR")
                .antMatchers(HttpMethod.GET, "/api/v1/pages").hasRole("ADMINISTRATOR")
                .antMatchers(HttpMethod.PUT, "/api/v1/pages/**").hasRole("ADMINISTRATOR")
                .antMatchers(HttpMethod.DELETE, "/api/v1/pages/**").hasRole("ADMINISTRATOR")
                .antMatchers(HttpMethod.PUT, "/api/v1/site").hasRole("ADMINISTRATOR")
                .anyRequest().authenticated();

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.headers().frameOptions().disable();


    }


}
