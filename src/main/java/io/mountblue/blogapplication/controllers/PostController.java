package io.mountblue.blogapplication.controllers;


import io.mountblue.blogapplication.entity.Post;
import io.mountblue.blogapplication.entity.Tag;
import io.mountblue.blogapplication.entity.User;
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


    @GetMapping("/")
    public String processAllPosts(Model theModel){
        return  findPaginated(1,"title","asc",null,null,null,theModel);

    }

    // get post by id;
    @GetMapping("/post{postId}")
    public String retreivedPostById(@PathVariable Integer postId, Model model) {
        return postService.getPostById(postId,model);
    };

    @GetMapping("/newpost")
    public String createNewPost(@RequestParam("user") String user,Model model){
        return  postService.navigateNewPost(user,model);
    }

    @PostMapping("/createpost")
    public String processNewPost(@RequestParam("author") String author,@ModelAttribute("post") Post newPost,@ModelAttribute("tag")Tag newTag){
        System.out.println("Author" + author);
//        return "hello";
       return postService.createPost(author,newPost,newTag);
    }

    @GetMapping("/editpost{postId}")
    public String editPost(@PathVariable Integer postId,
                           Model model){
        return postService.navigateEditPost(postId,model);
    }

    @PostMapping("/updatepost")
    public String processUpdatedPost(@RequestParam("author") String author,@ModelAttribute("post")Post updatedPost,@ModelAttribute("tags")String updatedTags,
                                     @ModelAttribute("id")int postId,Model model){
        System.out.println("update");
        System.out.println(author);
        return postService.updatePost(author,updatedPost,updatedTags,postId,model);
    }

    @GetMapping("/deletepost{postId}")
    public String processDeletePost(@PathVariable int postId){
        return postService.deletePost(postId);
    }



    @GetMapping("/change")
    public String processSortPost(@RequestParam("sort") String sortBy,Model theModel,RedirectAttributes redirectAttributes){
      return postService.sortPost(sortBy,theModel,redirectAttributes);
    }


    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam(value = "sortField",required = false) String sortField,
                                @RequestParam(value = "sortDir",required = false) String sortDir,
                                @RequestParam(name = "author_filter",required = false) String authorFilter,
                                @RequestParam(name = "tag_filter",required = false) String tagFilter,
                                @RequestParam(name="search",required = false) String search,
                                Model model) {
        int pageSize = 3;

        return postService.findPaginated(pageNo,pageSize,sortField,sortDir,authorFilter,tagFilter,search,model);
    }

}
