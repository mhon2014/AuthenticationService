package com.example.AuthenticationService.security.config;

import com.example.AuthenticationService.appuser.AppUserService;
import com.example.AuthenticationService.authentication.JWTService;
import com.example.AuthenticationService.security.CookieAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
public class WebSecurityConfig {

    private final AppUserService appUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JWTService jwtService;

//    @Value("{$Spring.cookie-name}")
//    private final String SessionCookie;

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


        return http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new CookieAuthFilter("SESSION", jwtService), UsernamePasswordAuthenticationFilter.class)
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
    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(appUserService);
        return new ProviderManager(provider);
    }


    //ignore filters for these paths
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer(){
//        return (web) -> web.ignoring().requestMatchers( "/","user/**", "user/signup");
//    }

}
