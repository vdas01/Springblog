package io.mountblue.blogapplication.services;

import io.mountblue.blogapplication.entity.Post;
import io.mountblue.blogapplication.entity.Tag;
import io.mountblue.blogapplication.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

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

    public String getNewPost(Model model){
        Post newPost = new Post();
        Tag newTag = new Tag();

        model.addAttribute("post",newPost);
        model.addAttribute("tag",newTag);

        return "CreatePost";
    }

    public String postCreation(Post newPost, Tag newTag){
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


}
