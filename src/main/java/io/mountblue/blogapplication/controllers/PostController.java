package io.mountblue.blogapplication.controllers;


import io.mountblue.blogapplication.entity.Post;
import io.mountblue.blogapplication.entity.Tag;
import io.mountblue.blogapplication.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@SessionAttributes("editing")
public class
PostController {
    PostService postService;
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

//    @GetMapping("/")
//    public String home(){
//
//    }
//

    @GetMapping("/")
    public String processAllPosts(Model theModel){
        return  findPaginated(1,"title","asc",null,null,theModel);

    }

    // get post by id;
    @GetMapping("/post{postId}")
    public String retreivedPostById(@PathVariable Integer postId, Model model) {
        return postService.getPostById(postId,model);
    };

    @GetMapping("/newpost")
    public String createNewPost(Model model){
        return  postService.navigateNewPost(model);
    }

    @PostMapping("/createpost")
    public String processNewPost(@ModelAttribute("post") Post newPost,@ModelAttribute("tag")Tag newTag){
        return postService.createPost(newPost,newTag);
    }

    @GetMapping("/editpost{postId}")
    public String editPost(@PathVariable Integer postId, Model model){
        return postService.navigateEditPost(postId,model);
    }

    @PostMapping("/updatepost")
    public String processUpdatedPost(@ModelAttribute("post")Post updatedPost,@ModelAttribute("tags")String updatedTags,
                                     @ModelAttribute("id")int postId,Model model){
        System.out.println(updatedTags);
        return postService.updatePost(updatedPost,updatedTags,postId,model);
    }

    @GetMapping("/deletepost{postId}")
    public String processDeletePost(@PathVariable int postId){
        return postService.deletePost(postId);
    }

//    @GetMapping("/getAll")
//    public ResponseEntity<List<Post>> getAllposts() {
//        List<Post> posts = postRepository.findAll();
//        return new ResponseEntity<>(posts, HttpStatus.OK);
//    }

//    @GetMapping("/editTitle")
//    public ResponseEntity<Post> editTitle(@RequestParam Integer postId) {
//
//        Post fromDB = postRepository.findById(postId).get();
//
//        return new ResponseEntity<>(fromDB, HttpStatus.OK);
//
//    }

//    @GetMapping("/editForm")
//    public String editForm(Model model, @RequestParam Integer postId) {
//
//        Post fromDB = postRepository.findById(postId).get();
//
//        model.addAttribute("toUpdatePost",  fromDB);
//
//
//        return "UpdatePost";
//
//    }

    @GetMapping("/change")
    public String processSortPost(@RequestParam("sort") String sortBy,Model theModel,RedirectAttributes redirectAttributes){
      return postService.sortPost(sortBy,theModel,redirectAttributes);
    }

    @GetMapping("/search")
    public String processSearch(@RequestParam(defaultValue =  "0")int page,@RequestParam("search")String search,Model theModel){

        return "Home";
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam(value = "sortField",required = false) String sortField,
                                @RequestParam(value = "sortDir",required = false) String sortDir,@RequestParam(name = "author_filter",required = false) String authorFilter,
                                @RequestParam(name = "tag_filter",required = false) String tagFilter,
                                Model model) {
        int pageSize = 3;

        return postService.findPaginated(pageNo,pageSize,sortField,sortDir,authorFilter,tagFilter,model);
    }

}
