package io.mountblue.blogapplication.repository;

import io.mountblue.blogapplication.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;



public interface RolesRepository extends JpaRepository<Roles,Integer> {
}
