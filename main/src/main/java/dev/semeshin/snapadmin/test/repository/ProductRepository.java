package dev.semeshin.snapadmin.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.semeshin.snapadmin.test.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
