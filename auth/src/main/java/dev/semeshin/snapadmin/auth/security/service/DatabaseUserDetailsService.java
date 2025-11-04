package dev.semeshin.snapadmin.auth.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import dev.semeshin.snapadmin.auth.models.User;
import dev.semeshin.snapadmin.auth.repository.AuthUserRepository;
import dev.semeshin.snapadmin.auth.security.dto.DatabaseUserDetails;

import java.util.Optional;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
	private final AuthUserRepository userRepository;

	public DatabaseUserDetailsService(AuthUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUsername(username);
		if (user.isPresent()) {
			return new DatabaseUserDetails(user.get());
		} else {
			throw new UsernameNotFoundException("Username not found");
		}
	}
}