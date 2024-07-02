package com.example.movietickets.demo;

import com.example.movietickets.demo.service.OauthService;
import com.example.movietickets.demo.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;


@Configuration // Đánh dấu lớp này là một lớp cấu hình cho Spring Context.
@EnableWebSecurity // Kích hoạt tính năng bảo mật web của Spring Security.
@RequiredArgsConstructor // Lombok tự động tạo constructor có tham số cho tất cả các trường final.


public class SecurityConfig {

    private final UserService userService;
    private final OauthService oauthService;

    @Bean
    public UserDetailsService userDetailsService() {
        return userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        var auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService());
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(@NotNull HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/assets/**", "/css/**", "/js/**", "/", "/oauth/**",
                                "/register", "/error", "/purchase", "/films", "/films/film-details/**", "/schedules/**", "/films/by-country", "/films/by-category", "/films/by-search", "/purchase/history",
                                "/cart", "/cart/**", "/blog", "blog/details", "/popcorn", "/movie/details", "/movie/seat-plan", "/feedback", "/blog", "/blog/blog-details", "/about", "blog/blog-details/{id}/comment")
                        .permitAll()
                        .requestMatchers("admin/schedules/edit/**", "/admin/schedules/add", "/admin/schedules", "/admin/seats", "/admin/films", "/admin/films/edit", "/admin/films/add",
                                "/admin/countries", "/admin/countries/add", "/admin/countries/edit",
                                "/admin/categories/add", "/admin/categories", "/admin/categories/edit", "/admin/blog/add", "/admin/blog/delete", "/admin/blog/update", "/admin/comboFood/delete", "/admin/comboFood/update", "/admin/comboFood", "/admin/comboFood/add", "/admin/seatPrice", "/admin/seatPrice/add", "/admin/seatPrice/edit", "/admin/seatPrice/delete", "blog/blog-details/{id}/delete")
                        .hasAnyAuthority("admin")
                        .requestMatchers("/api/**")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error")
                        .permitAll()
                )

                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/login")
                        .failureUrl("/login?error")
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                .userService(oauthService))
                        .successHandler((request, response, authentication) -> {
                            var oauthToken = (OAuth2AuthenticationToken) authentication;
                            var principal = authentication.getPrincipal();

                            String email = null;
                            String username = null;

                            if (principal instanceof DefaultOidcUser) {
                                DefaultOidcUser oidcUser = (DefaultOidcUser) principal;
                                email = oidcUser.getEmail();
                                username = oidcUser.getName();
                            } else if (principal instanceof DefaultOAuth2User) {
                                DefaultOAuth2User oauth2User = (DefaultOAuth2User) principal;
                                email = oauth2User.getAttribute("email");
                                username = oauth2User.getAttribute("name");
                            }

                            String provider = oauthToken.getAuthorizedClientRegistrationId().toUpperCase();
                            userService.saveOauthUser(email, username, provider);
                            response.sendRedirect("/");
                        })
                        .permitAll()
                )

                .rememberMe(rememberMe -> rememberMe
                        .key("3anhem")
                        .rememberMeCookieName("3anhem")
                        .tokenValiditySeconds(24 * 60 * 60)
                        .userDetailsService(userDetailsService())
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/404")
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .maximumSessions(1)
                        .expiredUrl("/login")
                )
                .httpBasic(httpBasic -> httpBasic
                        .realmName("3anhem")
                )
                .build();
    }

}
