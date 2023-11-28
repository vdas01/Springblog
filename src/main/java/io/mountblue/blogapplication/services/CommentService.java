package io.mountblue.blogapplication.services;


import io.mountblue.blogapplication.entity.Comment;
import io.mountblue.blogapplication.entity.Tag;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;

public interface CommentService {
        String addComment(String user,Integer postId, String name);

        String editComment(long commentId,int  postId, Model model);

        String updateComment(String editedComment, Integer commentId, int  postId, SessionStatus sessionStatus);

        String deleteComment(Integer commentId,Integer postId);

        List<Comment> getAllCommentsRest();

        Comment getCommentByIdRest(int commentId);

        List<Comment> getCommentsByPostIdRest(int postId);

        String createCommentRest(int postId,Comment comment);

        String editCommentRest(int commentId,Comment comment);

        String deleteCommentRest(int commentId);
}
