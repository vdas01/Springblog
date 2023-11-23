package io.mountblue.blogapplication.services;

import io.mountblue.blogapplication.entity.Post;
import io.mountblue.blogapplication.entity.Tag;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

public interface PostService {

    String findAllPosts(int page,List<Post>tempPost,Model theModel);

    String getPostById(int postId, Model model);

    String navigateNewPost(Model model);

    String createPost(Post newPost, Tag newTag);

    String navigateEditPost(int postId,Model model);

    String updatePost(Post updatedPost,Tag updatedTag,int postId,Model model);

    String deletePost(int postId);

    String sortPost(String sortBy, Model theModel, RedirectAttributes redirectAttributes);
}
