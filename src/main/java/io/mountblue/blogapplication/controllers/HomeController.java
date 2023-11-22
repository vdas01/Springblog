package io.mountblue.blogapplication.controllers;


import io.mountblue.blogapplication.dao.AppDAO;
import io.mountblue.blogapplication.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    AppDAO appDAO;

    @GetMapping("/")
    public String handleHome(Model theModel){
        List<Post> posts = appDAO.findAllPosts();
        theModel.addAttribute("posts",posts);

        return "Home";
    }
}
