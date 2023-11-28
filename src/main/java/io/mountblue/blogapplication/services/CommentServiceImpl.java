package io.mountblue.blogapplication.services;

import io.mountblue.blogapplication.entity.Comment;
import io.mountblue.blogapplication.entity.Post;
import io.mountblue.blogapplication.entity.User;
import io.mountblue.blogapplication.repository.CommentRepository;
import io.mountblue.blogapplication.repository.PostRepository;
import io.mountblue.blogapplication.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public CommentServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CommentServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public CommentServiceImpl(){}

    public String addComment(String user,Integer postId,String text){
        Optional<Post> optionalPost = postRepository.findById(postId);
        if(optionalPost.isPresent()){
            User newuser = userRepository.findByName(user);

            Comment newComment = new Comment(newuser.getName(),newuser.getEmail(),text);
            Post post = optionalPost.get();
            newComment.setPost(post);
            newComment.setUser(newuser);

            commentRepository.save(newComment);
        }

        return "redirect:/post"+postId;
    }

    public String editComment(long commentId,int  postId, Model model){
        model.addAttribute("editing",String.valueOf(commentId));
        return "redirect:/post" + postId ;
    }

    @Override
    public String updateComment(String editedComment, Integer commentId, int postId, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        Comment oldComment = commentRepository.findById(commentId).get();
        oldComment.setComment(editedComment);
        commentRepository.save(oldComment);
        return "redirect:/post" + postId;
    }

    @Override
    public String deleteComment(Integer commentId,Integer postId) {
        Comment comment = commentRepository.findById(commentId).get();
        commentRepository.delete(comment);
        return "redirect:/post" + postId;
    }

    @Override
    public List<Comment> getAllCommentsRest() {
            return commentRepository.findAll();
    }

    @Override
    public Comment getCommentByIdRest(int commentId) {
        Comment comment = null;
        Optional<Comment> retirvedCommentById = commentRepository.findById(commentId);

        if(retirvedCommentById.isPresent()){
            comment = retirvedCommentById.get();
        }

        return  comment;
    }

    @Override
    public List<Comment> getCommentsByPostIdRest(int postId) {
        List<Comment> comments = null;
        Optional<Post> retirvedPostById = postRepository.findById(postId);

        if(retirvedPostById.isPresent()){
            Post post = retirvedPostById.get();
            comments = post.getComments();
        }
        return comments;
    }

    @Override
    @Transactional
    public String createCommentRest(int postId,Comment comment) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();

            Optional<Post> retrivedPostById = postRepository.findById(postId);
            if(retrivedPostById.isPresent()){
                User newuser = userRepository.findByName(username);
                comment.setUser(newuser);
                comment.setEmail(newuser.getEmail());

                Post post = retrivedPostById.get();
                comment.setPost(post);

                commentRepository.save(comment);

                return "created";
            } else{
                return "Post not found";
            }
        } else {
            return "Unauthorized";
        }

    }

    @Override
    @Transactional
    public String editCommentRest(int commentId, Comment comment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String role = authentication.getAuthorities().toString();
            String username = authentication.getName();
            Optional<Comment> retrivedCommentById = commentRepository.findById(commentId);
            if (retrivedCommentById.isPresent()) {
                Comment oldComment = retrivedCommentById.get();
                oldComment.setComment(comment.getComment());

                String author = comment.getName();
                if (author.equals(username) || role.equals("[ROLE_admin]")){
                    commentRepository.save(oldComment);
                    return "Edited";
                } else{
                    return "Forbidden";
                }
            } else {
                return "Comment not found";
            }
        } else{
            return "Unauthorized";
        }
    }

    @Override
    public String deleteCommentRest(int commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String role = authentication.getAuthorities().toString();
            String username = authentication.getName();

            Optional<Comment> retirevedComment = commentRepository.findById(commentId);
            if(retirevedComment.isPresent()) {
                Comment deleteComment = retirevedComment.get();

                String author = deleteComment.getName();

                if (author.equals(username) || role.equals("[ROLE_admin]")) {
                    postRepository.deleteById(commentId);
                    return "deleted";
                } else {
                    return "Unauthorized";
                }

            } else {
                return "Comment not found";
            }
        } else {
            return "Unauthenticated";
        }
    }


}
