package io.mountblue.blogapplication.controllers;


import io.mountblue.blogapplication.entity.Post;
import io.mountblue.blogapplication.entity.Tag;
import io.mountblue.blogapplication.repository.PostRepository;
import io.mountblue.blogapplication.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class PostController {
    PostService postService;
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // get post by id;
    @GetMapping("/post{postId}")
    public String retreivedPostById(@PathVariable Integer postId, Model model) {
        return postService.getPostById(postId,model);
    };

    @GetMapping("/newpost")
    public String createNewPost(Model model){
        return  postService.navigateNewPost(model);
    }

    @PostMapping("/createpost")
    public String processNewPost(@ModelAttribute("post") Post newPost,@ModelAttribute("tag")Tag newTag){
        return postService.createPost(newPost,newTag);
    }

    @GetMapping("/editpost{postId}")
    public String editPost(@PathVariable Integer postId, Model model){
        return postService.navigateEditPost(postId,model);
    }

    @GetMapping("/updatepost")
    public String processUpdatedPost(@ModelAttribute("post")Post updatedPost,@ModelAttribute("tag")Tag updatedTag,
                                     Model model){
        return postService.updatePost(updatedPost,updatedTag,model);
    }


}
