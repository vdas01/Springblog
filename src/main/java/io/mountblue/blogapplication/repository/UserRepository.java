package io.mountblue.blogapplication.repository;

import io.mountblue.blogapplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

    User findByName(String username);
    User findByEmail(String email);
}
