package io.mountblue.blogapplication.rest;

import io.mountblue.blogapplication.entity.Post;
import io.mountblue.blogapplication.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<Post>> getAllPostsRest(){
        List<Post> posts = postService.findAllPostsRest();
        if(posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable int postId){
            Post post = postService.findPostByIdRest(postId);
            if(post == null){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PostMapping("/posts")
    public ResponseEntity<String> createPost(@RequestBody Post post) {
            String mssg = postService.createPostRest(post);
            if(mssg.equals("created")){
                return new ResponseEntity<>("Post created successfully", HttpStatus.CREATED);
            }
            else if(mssg.equals("Unauthorized")){
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>("Null",HttpStatus.NOT_IMPLEMENTED);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable int postId,@RequestBody Post updatedPost){
        String message = postService.updatePostRest(postId,updatedPost);

        if(message.equals("Edited")){
            return new ResponseEntity<>("Post Edited",HttpStatus.OK);
        } else if(message.equals("Post not found")){
            return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
        } else if(message.equals("Forbidden")){
            return new ResponseEntity<>("Unauthorized",HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable int postId){
        String message = postService.deletePostRest(postId);

        if(message.equals("deleted")){
            return new ResponseEntity<>("Post deleted successfully", HttpStatus.OK);
        } else if(message.equals("Unauthorized")){
            return new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN);
        } else if(message.equals("Post not found")){
            return new ResponseEntity<>("Post not found", HttpStatus.NO_CONTENT);
        } else{
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }




}
