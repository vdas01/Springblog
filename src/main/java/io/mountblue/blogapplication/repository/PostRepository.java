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

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN p.tags t ON COALESCE(t.name, '') IN :tagsFilter " +
            "WHERE (:authorFilter IS NULL OR p.author IN :authorFilter) AND " +
            " (:tagsFilter IS NULL OR t.name IN :tagsFilter) " +
//            "AND (:publishedDateFilter IS NULL OR p.publishedAt = :publishedDateFilter) " +
            "ORDER BY " +
            "CASE WHEN :sortField = 'publishedAt' AND :sortDirection = 'asc' THEN p.publishedAt END ASC, " +
            "CASE WHEN :sortField = 'publishedAt' AND :sortDirection = 'desc' THEN p.publishedAt END DESC, " +
            "CASE WHEN :sortField = 'title' AND :sortDirection = 'asc' THEN p.title END ASC, " +
            "CASE WHEN :sortField = 'title' AND :sortDirection = 'desc' THEN p.title END DESC, " +
            "CASE WHEN :sortField = 'author' AND :sortDirection = 'asc' THEN p.author END ASC, " +
            "CASE WHEN :sortField = 'author' AND :sortDirection = 'desc' THEN p.author END DESC, " +
            "CASE WHEN :sortField = 'excerpt' AND :sortDirection = 'asc' THEN p.excerpt END ASC, " +
            "CASE WHEN :sortField = 'excerpt' AND :sortDirection = 'asc' THEN p.excerpt END desc, " +
            "CASE WHEN :sortDirection IS NULL THEN p.title END"
    )
    Page<Post> filterPosts(@Param("authorFilter") String authorFilter, @Param("tagsFilter") List<String> tagFilter,
                           @Param("sortField") String sortField,
                           @Param("sortDirection") String sortDirection,Pageable pageable);

}
