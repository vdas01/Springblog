package io.mountblue.blogapplication.services;

import io.mountblue.blogapplication.entity.*;
import io.mountblue.blogapplication.repository.PostRepository;
import io.mountblue.blogapplication.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements  TagService{

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostRepository postRepository;



    public TagServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public TagServiceImpl(){};
    @Override
    public List<Tag> findAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public Tag getTagByIdRest(int tagId) {
        Tag tag = null;
        Optional<Tag> retirvedTagById = tagRepository.findById(tagId);
        if(retirvedTagById.isPresent()){
            tag = retirvedTagById.get();
        }
        return tag;
    }

    @Override
    public String createTagRest(int postId,Tag tag) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {

            Optional<Post> retrievedPostById = postRepository.findById(postId);
            if(retrievedPostById.isPresent()){
                Post post = retrievedPostById.get();
                tag.addPost(post);
                tag.setId(0);

                tagRepository.save(tag);

                return "created";
            } else{
                return "Post not found";
            }
        } else {
            return "Unauthorized";
        }
    }

    @Override
    public List<Tag> getTagsByPostIdRest(int postId) {
        List<Tag> tags = null;
        Optional<Post> retirevedPostById = postRepository.findById(postId);

        if(retirevedPostById.isPresent()){
            Post post = retirevedPostById.get();
            tags = post.getTags();
        }
        return tags;
    }

    @Override
    public String updateTagRest(int tagId, Tag updatedTag) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String role = authentication.getAuthorities().toString();
            String username = authentication.getName();
            Optional<Tag> retrivedTagById = tagRepository.findById(tagId);
            if (retrivedTagById.isPresent()) {
                Tag oldTag = retrivedTagById.get();
                oldTag.setName(updatedTag.getName());
                updatedTag.setId(tagId);

                if (role.equals("[ROLE_admin]")){
                    tagRepository.save(oldTag);
                    return "Updated";
                } else{
                    return "Forbidden";
                }
            } else {
                return "Tag not found";
            }
        } else{
            return "Unauthorized";
        }
    }

    @Override
    public String deleteTagRest(int tagId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String role = authentication.getAuthorities().toString();

            Optional<Tag> retirevedTagById = tagRepository.findById(tagId);
            if(retirevedTagById.isPresent()) {
                Tag deleteTag = retirevedTagById.get();

                if (role.equals("[ROLE_admin]")) {
                    tagRepository.deleteById(tagId);
                    return "deleted";
                } else {
                    return "Unauthorized";
                }

            } else {
                return "Tag not found";
            }
        } else {
            return "Unauthenticated";
        }
    }


}
