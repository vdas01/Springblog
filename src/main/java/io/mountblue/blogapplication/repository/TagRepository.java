package io.mountblue.blogapplication.repository;

import io.mountblue.blogapplication.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag,Integer> {
}
