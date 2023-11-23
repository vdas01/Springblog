package io.mountblue.blogapplication.repository;

import io.mountblue.blogapplication.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {

    List<Post> findAllByOrderByTitleAsc();

    List<Post> findAllByOrderByAuthorAsc();

    List<Post> findAllByOrderByCreatedAtAsc();

    List<Post> findAllByOrderByCreatedAtDesc();
}
