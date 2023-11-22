package io.mountblue.blogapplication.services;

import io.mountblue.blogapplication.entity.Post;
import io.mountblue.blogapplication.entity.Tag;
import org.springframework.ui.Model;

public interface PostService {

    String getPostById(int postId, Model model);

    String navigateNewPost(Model model);

    String createPost(Post newPost, Tag newTag);

    String navigateEditPost(int postId,Model model);

    String updatePost(Post updatedPost,Tag updatedTag,int postId,Model model);

}
