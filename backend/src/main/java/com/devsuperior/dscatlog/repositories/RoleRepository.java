package com.devsuperior.dscatlog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsuperior.dscatlog.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

}
