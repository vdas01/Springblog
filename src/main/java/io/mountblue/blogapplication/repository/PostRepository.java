package io.mountblue.blogapplication.repository;

import io.mountblue.blogapplication.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {

    List<Post> findAllByOrderByTitleAsc();

    List<Post> findAllByOrderByAuthorAsc();

    List<Post> findAllByOrderByCreatedAtAsc();

    List<Post> findAllByOrderByCreatedAtDesc();

    Page<Post> findAllBy(Pageable pageable);

}
