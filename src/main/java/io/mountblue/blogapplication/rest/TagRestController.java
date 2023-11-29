package io.mountblue.blogapplication.rest;


import io.mountblue.blogapplication.entity.Comment;
import io.mountblue.blogapplication.entity.Tag;
import io.mountblue.blogapplication.repository.TagRepository;
import io.mountblue.blogapplication.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TagRestController {

    @Autowired
    TagService tagService;

    public TagRestController(){

    }

    public TagRestController( TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/tags")
    public ResponseEntity<List<Tag>> getAllTags(){
        List<Tag> tags = tagService.findAllTags();
        if(tags.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else{
            return new ResponseEntity<>(tags,HttpStatus.OK);
        }
    }

    @GetMapping("/tags/{tagId}")
    public ResponseEntity<Tag> getTagById(@PathVariable int tagId){
        Tag tag = tagService.getTagByIdRest(tagId);
        if(tag == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(tag,HttpStatus.OK);
        }
    }

    @GetMapping("/tags/postId/{postId}")
    public ResponseEntity<List<Tag>> getTagsByPostId(@PathVariable int postId){
        List<Tag> tags = tagService.getTagsByPostIdRest(postId);

        if(tags == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if(tags.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else{
            return new ResponseEntity<>(tags,HttpStatus.OK);
        }
    }

    @PostMapping("/tags/postId/{postId}")
    public ResponseEntity<String> createTag(@PathVariable int postId,@RequestBody Tag tag){
        String message = tagService.createTagRest(postId,tag);
        if(message.equals("created")){
            return new ResponseEntity<>("Tag created successfully", HttpStatus.CREATED);
        }
        else if(message.equals("Unauthorized")){
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }else {
            return new ResponseEntity<>("Post not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/tags/{tagId}")
    public ResponseEntity<String> updateTag(@PathVariable int tagId, @RequestBody Tag updatedTag){
        String message = tagService.updateTagRest(tagId,updatedTag);

        if(message.equals("Updated")){
            return new ResponseEntity<>("Tag Updated",HttpStatus.OK);
        } else if(message.equals("Tag not found")){
            return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
        } else if(message.equals("Forbidden")){
            return new ResponseEntity<>("Unauthorized",HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/tags/{tagId}")
    public ResponseEntity<String> deleteTag(@PathVariable int tagId){
        String message = tagService.deleteTagRest(tagId);

        if(message.equals("deleted")){
            return new ResponseEntity<>("Tag deleted successfully", HttpStatus.OK);
        } else if(message.equals("Unauthorized")){
            return new ResponseEntity<>("Unauthorized", HttpStatus.FORBIDDEN);
        } else if(message.equals("Tag not found")){
            return new ResponseEntity<>("Tag not found", HttpStatus.NOT_FOUND);
        } else{
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }


}
