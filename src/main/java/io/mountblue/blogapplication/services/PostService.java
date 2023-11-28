package io.mountblue.blogapplication.services;

import io.mountblue.blogapplication.entity.Post;
import io.mountblue.blogapplication.entity.Tag;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

public interface PostService {

    String findAllPosts(int page,String sortBy,List<Post>tempPost,Model theModel);

    String getPostById(int postId, Model model);

    void navigateNewPost(String user, Model model);

    void createPost(String author,Post newPost, Tag newTag);

    void navigateEditPost(int postId,Model model);

    void updatePost(String author,Post updatedPost,String updatedTag,int postId,Model model);

    void deletePost(int postId);

    void sortPost(String sortBy, Model theModel, RedirectAttributes redirectAttributes);


    void findPaginated(int pageNo, int pageSize, String sortField, String sortDirection, String authorFilter, String tagFilter, String search,Model model);

    List<Post> findAllPostsRest();

    Post findPostByIdRest(int postId);

    String createPostRest(Post post);

    String deletePostRest(int postId);

    String updatePostRest(int postId,Post updatedPost);

//    Page<Post> searchPosts(int page,String search,Model theModel);
}
