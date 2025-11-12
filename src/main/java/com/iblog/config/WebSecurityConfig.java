package com.fz.config;

import com.fz.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;

/**
 * @author fz
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserService userService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // enable CORS support (will use WebMvcConfigurer/CorsConfiguration)
        http.cors();
    http.authorizeRequests()
        // permit public resources and common API used by frontend without login
    .antMatchers("/blogimg/**", "/index.html", "/static/**", "/login_page", "/login", "/reg", "/auth/**", "/", "/currentUserName", "/currentUserEmail", "/isAdmin", "/category/**").permitAll()
        // protect write-related article endpoints (require authentication)
    .antMatchers(HttpMethod.POST, "/article/").authenticated()
    .antMatchers(HttpMethod.POST, "/article/uploadimg").authenticated()
    .antMatchers(HttpMethod.PUT, "/article/dustbin").authenticated()
    .antMatchers(HttpMethod.GET, "/article/my", "/article/dataStatistics").authenticated()
        // allow public read access for listing and detail (other article endpoints should be authenticated as above)
    .antMatchers(HttpMethod.GET, "/article/all", "/article/*").permitAll()
        // admin endpoints require specific role
        .antMatchers("/admin/**").hasRole("超级管理员")
        // other requests require authentication
        .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login_page")
                .successHandler((request, response, authentication) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    out.write("{\"status\":\"success\",\"msg\":\"登录成功\"}");
                    out.flush();
                    out.close();
                })
                .failureHandler((request, response, e) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    out.write("{\"status\":\"error\",\"msg\":\"登录失败\"}");
                    out.flush();
                    out.close();
                })
                .loginProcessingUrl("/login")
                .usernameParameter("username").passwordParameter("password").permitAll()
                .and()
                .logout().permitAll()
                .and()
                .csrf().disable()
                .exceptionHandling()
                // for API/ajax calls return 401 JSON instead of redirecting to login page
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType("application/json;charset=utf-8");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    PrintWriter out = response.getWriter();
                    out.write("{\"status\":\"error\",\"msg\":\"未认证\"}");
                    out.flush();
                    out.close();
                }).accessDeniedHandler(getAccessDeniedHandler());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userService).passwordEncoder(passwordEncoder);
        return builder.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers("/blogimg/**", "/index.html", "/static/**");
    }


    @Bean
    AccessDeniedHandler getAccessDeniedHandler() {
        return new AuthenticationAccessDeniedHandler();
    }
}