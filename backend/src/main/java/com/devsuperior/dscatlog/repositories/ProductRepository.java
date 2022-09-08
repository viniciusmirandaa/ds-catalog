package com.devsuperior.dscatlog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsuperior.dscatlog.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

}
