package com.example.AuthenticationService.security.config;

import com.example.AuthenticationService.appuser.AppUserService;
import com.example.AuthenticationService.security.JWTService;
import com.example.AuthenticationService.security.CookieAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfig {

    private final AppUserService appUserService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JWTService jwtService;

    private final String cookieName;

    public WebSecurityConfig(AppUserService appUserService,
                             BCryptPasswordEncoder bCryptPasswordEncoder,
                             JWTService jwtService,
                             @Value("${spring.cookie-name}") String cookieName) {
        this.appUserService = appUserService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtService = jwtService;
        this.cookieName = cookieName;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /*
        * disable csrf for now
        * grant permission  all to hit the end point
        * authorize request
        * */

//        Security filter chain: [
//        DisableEncodeUrlFilter
//        WebAsyncManagerIntegrationFilter
//        SecurityContextHolderFilter
//        HeaderWriterFilter
//        CsrfFilter
//        LogoutFilter
//        RequestCacheAwareFilter
//        SecurityContextHolderAwareRequestFilter
//        AnonymousAuthenticationFilter
//        ExceptionTranslationFilter
//          ]

        CookieAuthFilter cookieAuthFilter = new CookieAuthFilter(cookieName, jwtService);
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(cookieAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests( auth -> {
                    auth.requestMatchers("/").permitAll();
//                    auth.requestMatchers("/user").permitAll(); //permit authenticated users
                    auth.requestMatchers("/user/*").permitAll();
                    auth.requestMatchers("/user/signin").permitAll();
                    auth.requestMatchers("/user/signout").permitAll();
                    auth.requestMatchers("/sessions/all").permitAll();
                    auth.anyRequest().authenticated();
                })
//                .formLogin( form -> form.loginPage("/user/signin").permitAll())
//                .logout( form -> form.logoutUrl("/user/signout")
//                            .deleteCookies(SessionCookie)
//                            .logoutSuccessUrl("/")
//                )

                .build();
    }


    //used for user authentication
    //Essentially get the users from the DaoAuthenticationProvider which
    //uses the appuserservice to get the users

    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(appUserService);
        return new ProviderManager(provider);
    }

//    ignore filters for these paths
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer(){
//        return (web) -> web.ignoring().requestMatchers( "/", "user/signup", "user/verify");
//    }

}
