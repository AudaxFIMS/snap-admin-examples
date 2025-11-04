package dev.semeshin.snapadmin.auth.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import tech.ailef.snapadmin.external.SnapAdminProperties;

@Component
@ConditionalOnProperty(name = "snapadmin.enabled", havingValue = "true")
public class SnapadminAuthFilter extends OncePerRequestFilter {
    private final SnapAdminProperties snapAdminProperties;

    public SnapadminAuthFilter(SnapAdminProperties snapAdminProperties1) {
        this.snapAdminProperties = snapAdminProperties1;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Only apply for snap-admin urls except do-login, login
        return !request.getServletPath().startsWith("/" + snapAdminProperties.getBaseUrl())
                || request.getServletPath().startsWith("/" + snapAdminProperties.getBaseUrl() + "/login")
                || request.getServletPath().startsWith("/" + snapAdminProperties.getBaseUrl() + "/do-login");
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Map<String, Object> authData = (Map<String, Object>) session.getAttribute("snapAdminAuthData");
            if (authData != null) {
                Authentication authToken = new UsernamePasswordAuthenticationToken(
                        authData.get("user_name"),   // principal
                        authData.get("user_data"), // credentials
                        (Collection<? extends GrantedAuthority>) authData.get("user_roles") // roles/authorities
                );

                // Set authentication ONLY for this request
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                SecurityContextHolder.clearContext();
                response.sendRedirect(request.getContextPath() + "/" + snapAdminProperties.getBaseUrl() + "/login");
                return;
            }
        }
        filterChain.doFilter(request, response);
        SecurityContextHolder.clearContext(); // clear after request
    }
}