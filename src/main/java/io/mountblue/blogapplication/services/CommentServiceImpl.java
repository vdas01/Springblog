package io.mountblue.blogapplication.services;

import io.mountblue.blogapplication.entity.Comment;
import io.mountblue.blogapplication.entity.Post;
import io.mountblue.blogapplication.entity.User;
import io.mountblue.blogapplication.repository.CommentRepository;
import io.mountblue.blogapplication.repository.PostRepository;
import io.mountblue.blogapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        else{
            //error
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



}
