package com.devsuperior.dscatlog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsuperior.dscatlog.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{

}
