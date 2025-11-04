package dev.semeshin.snapadmin.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.GrantedAuthority;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import dev.semeshin.snapadmin.auth.security.service.SnapadminAuthService;
import tech.ailef.snapadmin.external.SnapAdminProperties;

import java.util.Collection;
import java.util.Map;

@Controller
@RequestMapping("/${snapadmin.baseUrl}")
@ConditionalOnProperty(name = "snapadmin.enabled", havingValue = "true")
public class SnapadminLoginController {
	private final SnapadminAuthService snapadminAuthService;
	private final SnapAdminProperties snapAdminProperties;

	public SnapadminLoginController(
			SnapadminAuthService snapadminAuthService,
			SnapAdminProperties snapAdminProperties
	) {
		this.snapadminAuthService = snapadminAuthService;
		this.snapAdminProperties = snapAdminProperties;
	}

	@GetMapping("/login")
	public String showLoginPage() {
		return "login";
	}

	@PostMapping("/do-login")
	public String processLogin(
			@RequestParam String username,
			@RequestParam String password,
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Model model
	) {
		try {
			Map<String, Object> authData = snapadminAuthService.authenticate(username, password);

			session.setAttribute("snapAdminAuthData", authData);

			Authentication auth = new UsernamePasswordAuthenticationToken(
					authData.get("user_name"), // principal
					authData.get("user_data"), // credentials
                    (Collection<? extends GrantedAuthority>) authData.get("user_roles") // roles/authorities
			);

			SecurityContextHolder.getContext().setAuthentication(auth);

			// Persist in session
			HttpSessionSecurityContextRepository repo = new HttpSessionSecurityContextRepository();
			repo.saveContext(SecurityContextHolder.getContext(), request, response);

			return "redirect:/" + snapAdminProperties.getBaseUrl();
		}catch (Exception e) {
			model.addAttribute("error", "Invalid credentials or API error");
			return "redirect:/" + snapAdminProperties.getBaseUrl() + "/login?error=true";
		}
	}

	@PostMapping("/logout")
	public String processLogin(
			HttpSession session
	) {
		session.removeAttribute("snapAdminAuthData");
		return "redirect:/" +  snapAdminProperties.getBaseUrl() + "/login";
	}
}