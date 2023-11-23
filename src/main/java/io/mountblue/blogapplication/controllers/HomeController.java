package io.mountblue.blogapplication.controllers;


import io.mountblue.blogapplication.dao.AppDAO;
import io.mountblue.blogapplication.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class HomeController {

    @Autowired
    AppDAO appDAO;

    public HomeController(AppDAO appDAO) {
        this.appDAO = appDAO;
    }

    @GetMapping("/")
    public String handleHome(@RequestParam(name = "tempPost",required = false) List<Post> tempPost, Model theModel){

        if(tempPost == null) {
            List<Post> posts = appDAO.findAllPosts();
            theModel.addAttribute("posts",posts);
        }
        else {
            theModel.addAttribute("posts",tempPost);
        }
        return "Home";
    }
}
