package io.mountblue.blogapplication.rest;

import io.mountblue.blogapplication.entity.Post;
import io.mountblue.blogapplication.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostRestController {

    @Autowired
    PostService postService;

    public PostRestController(){

    }

    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public List<Post> getAllPostsRest(){
        return  postService.findAllPostsRest();
    }

    @GetMapping("/posts/{postId}")
    public Post getPostById(){

    }


}
