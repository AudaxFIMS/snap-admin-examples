package dev.semeshin.snapadmin.auth.security.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import dev.semeshin.snapadmin.auth.security.filter.SnapadminAuthFilter;
import tech.ailef.snapadmin.external.SnapAdminProperties;

@Configuration
@EnableWebSecurity
@ConditionalOnProperty(name = "snapadmin.enabled", havingValue = "true")
public class SnapAdminSecurityConfig {
	private final SnapAdminProperties snapAdminProperties;

	public SnapAdminSecurityConfig(SnapAdminProperties snapAdminProperties) {
		this.snapAdminProperties = snapAdminProperties;
	}

	@Bean
	@Order(1)
	public SecurityFilterChain snapadminSecurityFilterChain(HttpSecurity http, SnapadminAuthFilter snapAdminAuthFilter) throws Exception {
		return http
				.csrf(AbstractHttpConfigurer::disable)
				.securityMatcher("/" + snapAdminProperties.getBaseUrl() + "/**")
				.authorizeHttpRequests(authorize -> authorize
					.requestMatchers(
							"/" + snapAdminProperties.getBaseUrl() + "/do-login",
							"/" + snapAdminProperties.getBaseUrl() + "/login",
							"/" + snapAdminProperties.getBaseUrl() + "/logout"
					).permitAll() // Allow OPTIONS calls
					.anyRequest().authenticated()
				)
				.addFilterBefore(snapAdminAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.formLogin(form -> form
						.loginPage("/" + snapAdminProperties.getBaseUrl() + "/login")
						.loginProcessingUrl("/" + snapAdminProperties.getBaseUrl() +"/login") // same POST URL
						.defaultSuccessUrl("/" + snapAdminProperties.getBaseUrl() + "/dashboard", true)
						.failureUrl("/" + snapAdminProperties.getBaseUrl() + "/login?error=true")
						.permitAll()
				)
				.build();
	}
}