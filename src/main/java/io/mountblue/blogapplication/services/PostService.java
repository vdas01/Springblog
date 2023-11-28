package io.mountblue.blogapplication.services;

import io.mountblue.blogapplication.entity.Post;
import io.mountblue.blogapplication.entity.Tag;
import io.mountblue.blogapplication.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

public interface PostService {

    String findAllPosts(int page,String sortBy,List<Post>tempPost,Model theModel);

    String getPostById(int postId, Model model);

    String navigateNewPost(String user, Model model);

    String createPost(String author,Post newPost, Tag newTag);

    String navigateEditPost(int postId,Model model);

    String updatePost(String author,Post updatedPost,String updatedTag,int postId,Model model);

    String deletePost(int postId);

    String sortPost(String sortBy, Model theModel, RedirectAttributes redirectAttributes);


    String findPaginated(int pageNo, int pageSize, String sortField, String sortDirection, String authorFilter, String tagFilter, String search,Model model);

    List<Post> findAllPostsRest();

//    Page<Post> searchPosts(int page,String search,Model theModel);
}
