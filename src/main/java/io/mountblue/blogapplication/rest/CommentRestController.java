package io.mountblue.blogapplication.rest;

import io.mountblue.blogapplication.entity.Comment;
import io.mountblue.blogapplication.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentRestController {

    @Autowired
    CommentService commentService;

    public CommentRestController(){

    }
    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> getAllComments(){
        List<Comment> comments = commentService.getAllCommentsRest();

        if(comments.isEmpty()){
            return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return  new ResponseEntity<>(comments,HttpStatus.OK);
        }
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<Comment> getCommentById(@PathVariable int commentId){
        Comment comment = commentService.getCommentByIdRest(commentId);

        if(comment == null){
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(comment,HttpStatus.OK);
        }
    }

    @GetMapping("/comments/postId/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable int postId){
        List<Comment> comments = commentService.getCommentsByPostIdRest(postId);

        if(comments == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if(comments.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else{
            return new ResponseEntity<>(comments,HttpStatus.OK);
        }
    }

    @PostMapping("/comments/{postId}")
    public ResponseEntity<String> createComment(@PathVariable int postId,@RequestBody Comment comment){
            String message = commentService.createCommentRest(postId,comment);

            if(message.equals("created")){
                return new ResponseEntity<>("Comment created",HttpStatus.CREATED);
            } else if(message.equals("Post not found")){
                return new ResponseEntity<>("Post not found",HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED);
            }
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<String> editComment(@PathVariable int commentId,@RequestBody Comment comment){
        String message = commentService.editCommentRest(commentId,comment);

        if(message.equals("Edited")){
            return new ResponseEntity<>("Comment Edited",HttpStatus.OK);
        } else if(message.equals("Comment not found")){
            return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
        } else if(message.equals("Forbidden")){
            return new ResponseEntity<>("Unauthorized",HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable int commentId){
        String message = commentService.deleteCommentRest(commentId);

        if(message.equals("deleted")){
            return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
        } else if(message.equals("Unauthorized")){
            return new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN);
        } else if(message.equals("Comment not found")){
            return new ResponseEntity<>("Comment not found", HttpStatus.NOT_FOUND);
        } else{
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

}
