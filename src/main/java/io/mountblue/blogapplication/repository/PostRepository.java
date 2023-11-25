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
            "(:tagsFilter IS NULL OR t.name IN :tagsFilter) " +
            "ORDER BY " +
            "CASE WHEN :sortField = 'publishedAt' AND :sortDir = 'asc' THEN p.publishedAt END ASC, " +
            "CASE WHEN :sortField = 'publishedAt' AND :sortDir = 'desc' THEN p.publishedAt END DESC, " +
            "CASE WHEN :sortField = 'title' AND :sortDir = 'asc' THEN p.title END ASC, " +
            "CASE WHEN :sortField = 'title' AND :sortDir = 'desc' THEN p.title END DESC, " +
            "CASE WHEN :sortField = 'author' AND :sortDir = 'asc' THEN p.author END ASC, " +
            "CASE WHEN :sortField = 'author' AND :sortDir = 'desc' THEN p.author END DESC, " +
            "CASE WHEN :sortField = 'excerpt' AND :sortDir = 'asc' THEN p.excerpt END ASC, " +
            "CASE WHEN :sortField = 'excerpt' AND :sortDir = 'desc' THEN p.excerpt END DESC, " +
            "CASE WHEN :sortDir IS NULL THEN p.title END")
    Page<Post> filterPosts(@Param("authorFilter") String authorFilter, @Param("tagsFilter") List<String> tagFilter,
                           @Param("sortField") String sortField,
                           @Param("sortDir") String sortDir, Pageable pageable);


    @Query("SELECT p FROM Post p JOIN PostTag pt ON p.Id = pt.postId JOIN Tag t ON pt.tagId = t.Id " +
            "WHERE p.title LIKE %:query% OR p.content LIKE %:query% OR p.author LIKE %:query% " +
            "OR p.excerpt LIKE %:query% OR t.name LIKE %:query%")
    Page<Post> searchPosts(@Param("query")String query,Pageable pageable);






}
