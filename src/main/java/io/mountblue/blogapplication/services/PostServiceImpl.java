package io.mountblue.blogapplication.services;

import io.mountblue.blogapplication.entity.Post;
import io.mountblue.blogapplication.entity.Tag;
import io.mountblue.blogapplication.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements  PostService{

    private PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostServiceImpl(){}

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


}