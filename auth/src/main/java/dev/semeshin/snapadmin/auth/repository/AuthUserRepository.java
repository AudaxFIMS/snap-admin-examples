package dev.semeshin.snapadmin.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.semeshin.snapadmin.auth.models.User;

public interface AuthUserRepository extends JpaRepository<User, Integer> {
	public Optional<User> findByUsername(String username);
}
