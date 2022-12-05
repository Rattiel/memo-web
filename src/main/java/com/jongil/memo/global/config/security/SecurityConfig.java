package com.jongil.memo.global.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
	private final LoginSuccessHandler loginSuccessHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		return http
				.authorizeRequests(authorizeRequests ->
						authorizeRequests
								.mvcMatchers("/css/**", "/js/**", "/library/**").permitAll()
								.mvcMatchers("/fragment").hasRole(UserRole.DEVELOPER.toString())
								.mvcMatchers("/h2-console/**").hasRole(UserRole.DEVELOPER.toString())
								.mvcMatchers("/image/upload/*/*/*/*").permitAll()
								.mvcMatchers("/image/upload").authenticated()
								.mvcMatchers("/login", "/register", "/logout").permitAll()
								.mvcMatchers("/post/new/create", "/post/*/update", "/post/*/remove").authenticated()
								.anyRequest().authenticated()
				).formLogin(formLogin ->
						formLogin
								.loginPage("/login")
								.loginProcessingUrl("/login")
								.successHandler(loginSuccessHandler)
								.permitAll()
				).logout(logout ->
						logout
								.logoutUrl("/logout")
								.logoutSuccessUrl("/login?logout")
								.invalidateHttpSession(true)
								.permitAll()
				).headers(headers ->
						headers
								.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
				).csrf(csrf ->
						csrf
								.ignoringAntMatchers("/h2-console/**")
				).build();
	}
}
