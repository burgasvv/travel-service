package org.burgas.travelservice.config

import org.burgas.travelservice.entity.identity.Authority
import org.burgas.travelservice.service.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig {

    private final val customUserDetailsService: CustomUserDetailsService
    private final val passwordEncoder: PasswordEncoder

    constructor(customUserDetailsService: CustomUserDetailsService, passwordEncoder: PasswordEncoder) {
        this.customUserDetailsService = customUserDetailsService
        this.passwordEncoder = passwordEncoder
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        val daoAuthenticationProvider = DaoAuthenticationProvider(this.customUserDetailsService)
        daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder)
        return ProviderManager(daoAuthenticationProvider)
    }

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .csrf { csrfConfigurer -> csrfConfigurer.csrfTokenRequestHandler(XorCsrfTokenRequestAttributeHandler()) }
            .cors { corsConfigurer -> corsConfigurer.configurationSource(UrlBasedCorsConfigurationSource()) }
            .httpBasic { httpBasicConfigurer ->
                httpBasicConfigurer.securityContextRepository(
                    RequestAttributeSecurityContextRepository()
                )
            }
            .authenticationManager(this.authenticationManager())
            .authorizeHttpRequests { requests ->
                requests

                    .requestMatchers(
                        "/api/v1/security/csrf-token",

                        "/api/v1/identities/create",

                        "/api/v1/countries", "/api/v1/countries/by-id",

                        "/api/v1/cities", "/api/v1/cities/by-id",

                        "/api/v1/sights", "/api/v1/sights/by-id",

                        "/api/v1/excursions", "/api/v1/excursions/by-id"
                    )
                    .permitAll()

                    .requestMatchers(
                        "/api/v1/identities/by-id", "/api/v1/identities/update", "/api/v1/identities/delete",
                        "/api/v1/identities/change-password", "/api/v1/identities/change-status"
                    )
                    .hasAnyAuthority(Authority.ADMIN.authority, Authority.USER.authority)

                    .requestMatchers(
                        "/api/v1/identities",

                        "/api/v1/countries/create", "/api/v1/countries/update", "/api/v1/countries/delete",

                        "/api/v1/cities/create", "/api/v1/cities/update", "/api/v1/cities/delete",

                        "/api/v1/sights/create", "/api/v1/sights/update", "/api/v1/sights/delete",
                        "/api/v1/sights/add-excursion", "/api/v1/sights/remove-excursion",

                        "/api/v1/excursions/create", "/api/v1/excursions/update", "/api/v1/excursions/delete"
                    )
                    .hasAnyAuthority(Authority.ADMIN.authority)
            }
            .build()
    }
}