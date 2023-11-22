package io.mountblue.blogapplication.dao;

import io.mountblue.blogapplication.entity.Post;

import java.util.List;

public interface AppDAO {



    List<Post> findAllPosts();
}
