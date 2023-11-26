package io.mountblue.blogapplication.controllers;


import io.mountblue.blogapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    UserService userService;


    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/loginPage")
    public String login(){
        return "Login";
    }

    @GetMapping("/register")
    public String register(){
        return "Registration";
    }

    @PostMapping("/registerUser")
    public String registerUser(@RequestParam(value = "name")String name,
                               @RequestParam(value = "email")String email,
                               @RequestParam(value = "password")String password){
        return userService.registerUser(name,email,password);
    }
}
