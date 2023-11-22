package io.mountblue.blogapplication.services;


import io.mountblue.blogapplication.entity.Comment;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

public interface CommentService {
        String addComment(Integer postId, String name);

        String editComment(long commentId,int  postId, Model model);

        String updateComment(String editedComment, Integer commentId, int  postId, SessionStatus sessionStatus);

        String deleteComment(Integer commentId,Integer postId);
}
