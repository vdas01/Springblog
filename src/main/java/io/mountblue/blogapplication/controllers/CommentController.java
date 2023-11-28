package io.mountblue.blogapplication.controllers;

import io.mountblue.blogapplication.entity.Comment;
import io.mountblue.blogapplication.entity.Post;
import io.mountblue.blogapplication.entity.Tag;
import io.mountblue.blogapplication.entity.User;
import io.mountblue.blogapplication.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.thymeleaf.expression.Strings;

import java.util.List;
import java.util.Optional;

@Controller
@SessionAttributes("editing")
public class CommentController {
    @Autowired
    CommentService commentService;

    private String editing = null;


    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //add comment
    @GetMapping("/createcomment{postId}")
    public String processAddComment(@RequestParam("user") String  user, @PathVariable Integer postId, @ModelAttribute("comment") String newComment){
        return commentService.addComment(user,postId,newComment);
    }

    @PostMapping("/editComment")
    public String processEditComment(@RequestParam Integer commentId,@RequestParam int  postId,Model model){
        return commentService.editComment(commentId,postId,model);
    }

    @PostMapping("/saveComment")
    public String processUpdateComment(@RequestParam String editedComment, @RequestParam Integer commentId,
                                       @RequestParam int  postId, SessionStatus sessionStatus){
        return commentService.updateComment(editedComment,commentId,postId,sessionStatus);
    }

    @GetMapping("/deleteComment/{commentId}/{postId}")
    public String processDeleteComment(@PathVariable Integer commentId,@PathVariable Integer postId){
        return commentService.deleteComment(commentId,postId);
    }


}
