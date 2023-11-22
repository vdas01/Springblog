package io.mountblue.blogapplication.dao;

import io.mountblue.blogapplication.entity.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class AppDAOImpl implements AppDAO{

    private EntityManager entityManager;

    @Autowired
    public AppDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public List<Post> findAllPosts() {
        TypedQuery<Post> typedQuery = entityManager.createQuery("from Post",Post.class);
        return typedQuery.getResultList();
    }
}
