package io.mountblue.blogapplication.services;

import io.mountblue.blogapplication.entity.Post;
import io.mountblue.blogapplication.entity.Tag;
import io.mountblue.blogapplication.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostService(){}

    public String getPostById(int postId, Model model){
        Optional<Post> retrievedPostById = postRepository.findById(postId);

        if (retrievedPostById.isPresent()) {

            Post post = retrievedPostById.get();
            model.addAttribute("inputPost", post);

            System.out.println("retreived post :- ");
            System.out.println(post.getId());
            System.out.println(post.getAuthor());
            System.out.println(post.getExcerpt());

        }
        return "Post";
    }

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

    public String navigateEditPost(int postId,Model model){
        Optional<Post> retrievedPostById = postRepository.findById(postId);
        if (retrievedPostById.isPresent()){
            Post oldPost = retrievedPostById.get();
            List<Tag> oldTags = oldPost.getTags();

            model.addAttribute("tags",oldTags);
            model.addAttribute("post",oldPost);
        }
        else{
            //error;
        }
        return "UpdatePost";
    }

    public String updatePost(Post updatedPost,Tag updatedTag,Model model){
        int postId = updatedPost.getId();
        //search old post by id
        //oldpost = post
       //remove old tags of post and add current tag
        //then merge
        Optional<Post> optionalPost = postRepository.findById(postId);

        if (optionalPost.isPresent()) {

            Post existingPost = optionalPost.get();
            existingPost.setTitle(updatedPost.getTitle());
            existingPost.setContent(updatedPost.getContent());
            existingPost.setExcerpt(updatedPost.getExcerpt());
            existingPost.setAuthor(updatedPost.getAuthor());
            existingPost.clearTags();

            String[] tagsArray = updatedTag.getName().split(",");
            for(String tempTag: tagsArray){
                Tag tag = new Tag(tempTag);
                existingPost.addTags(tag);
            }

             postRepository.save(existingPost);
        } else {
            // Handle the case where the post with the given ID is not found
//            throw new EntityNotFoundException("Post with ID " + postId + " not found");
        }
        return "Home";
    }


}
