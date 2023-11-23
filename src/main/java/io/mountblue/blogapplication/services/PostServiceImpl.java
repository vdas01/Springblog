package io.mountblue.blogapplication.services;

import io.mountblue.blogapplication.entity.Comment;
import io.mountblue.blogapplication.entity.Post;
import io.mountblue.blogapplication.entity.Tag;
import io.mountblue.blogapplication.repository.CommentRepository;
import io.mountblue.blogapplication.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements  PostService{

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    public PostServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostServiceImpl(){}

    @Override
    public String getPostById(int postId, Model model){
        Optional<Post> retrievedPostById = postRepository.findById(postId);

        if (retrievedPostById.isPresent()) {
            Post post = retrievedPostById.get();
            List<Tag> tags = post.getTags();
            Comment newComment = new Comment();
            List<Comment> comments = commentRepository.findAll();

            model.addAttribute("post", post);
            model.addAttribute("tags",tags);
            model.addAttribute("newComment",newComment);
            model.addAttribute("displaycomments",comments);
        }
        return "Post";
    }

    @Override
    public String navigateNewPost(Model model){
        Post newPost = new Post();
        Tag newTag = new Tag();

        model.addAttribute("post",newPost);
        model.addAttribute("tag",newTag);

        return "CreatePost";
    }

    public String createPost(Post newPost, Tag newTag){
        Post post = new Post();
        post = newPost;

        String[] tagsArray = newTag.getName().split(",");
        for(String tempTag: tagsArray){
            Tag tag = new Tag(tempTag);
            post.addTags(tag);
        }

        postRepository.save(post);
        return "redirect:/";
    }

    @Override
    public String navigateEditPost(int postId,Model model){
        Optional<Post> retrievedPostById = postRepository.findById(postId);
        if (retrievedPostById.isPresent()){
            Post oldPost = retrievedPostById.get();
            List<Tag> oldTagsList = oldPost.getTags();
            StringBuilder tags = new StringBuilder();

           for(Tag tempTag : oldTagsList){
               tags.append(tempTag.getName()).append(",");
           }
            tags.deleteCharAt(tags.length() - 1);

           model.addAttribute("postId",postId);
            model.addAttribute("tags",tags);
            model.addAttribute("post",oldPost);
        }
        else{
            //error;
        }
        return "UpdatePost";
    }

    @Override
    public String updatePost(Post updatedPost,Tag updatedTags,int postId,Model model){
            updatedPost.setId(postId);

            String[] tagsArray = updatedTags.getName().split(",");
            for(String tempTag: tagsArray){
                Tag tag = new Tag(tempTag);
                updatedPost.addTags(tag);
            }

            postRepository.save(updatedPost);

        return "redirect:/";
    }

    @Override
    public String deletePost(int postId) {
        if (postRepository.existsById(postId)) {
            postRepository.deleteById(postId);
        } else {
            // Handle the case where the post with the given ID is not found
        }
        return "redirect:/";
    }

    @Override
    public String sortPost(String sortBy,Model theModel, RedirectAttributes redirectAttributes) {
        List<Post> tempPost;

        if(sortBy.equals("title")){
            tempPost = postRepository.findAllByOrderByTitleAsc();
        }else if(sortBy.equals("author")){
            tempPost = postRepository.findAllByOrderByAuthorAsc();
        }else if(sortBy.equals("publishedAtAsc")){
            tempPost =  postRepository.findAllByOrderByCreatedAtAsc();
        }else{
            tempPost = postRepository.findAllByOrderByCreatedAtDesc();
        }

        redirectAttributes.addAttribute("tempPost", tempPost);
        return "redirect:/";
    }


}
