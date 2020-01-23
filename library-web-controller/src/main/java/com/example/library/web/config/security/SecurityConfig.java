package com.example.library.web.config.security;

import com.example.library.web.config.filter.SimpleCORSFilter;
import com.example.library.web.config.security.jwt.JwtConfigurer;
import com.example.library.web.config.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.session.SessionManagementFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final SimpleCORSFilter simpleCORSFilter;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider, SimpleCORSFilter simpleCORSFilter) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.simpleCORSFilter = simpleCORSFilter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(simpleCORSFilter, SessionManagementFilter.class)
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.DELETE, "/**/delete/**").hasRole("CREATOR")
                .antMatchers(HttpMethod.POST, "/books/**").hasAnyRole("ADMIN", "CREATOR")
                .antMatchers(HttpMethod.PUT, "/books/**").hasAnyRole("ADMIN", "CREATOR")
                .antMatchers(HttpMethod.GET, "/users/list/**").hasAnyRole("ADMIN", "CREATOR")
                .antMatchers(HttpMethod.POST, "/orders/**").hasAnyRole("USER", "ADMIN", "CREATOR")
                .antMatchers(HttpMethod.PUT, "/users/**").hasAnyRole("USER", "ADMIN", "CREATOR")
                .antMatchers(HttpMethod.GET, "/books/**", "/orders/**", "/users/username")
                .hasAnyRole("USER", "ADMIN", "CREATOR")
                .antMatchers("/auth/login", "/auth/logout", "/users/register", "/dictionaries/*")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }
}
