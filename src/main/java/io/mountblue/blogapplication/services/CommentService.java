package io.mountblue.blogapplication.services;


import io.mountblue.blogapplication.entity.Comment;

public interface CommentService {
        String addComment(Integer postId, String name);
}
