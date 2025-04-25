package com.example.urundegerlendirme.repository;

import com.example.urundegerlendirme.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
} 