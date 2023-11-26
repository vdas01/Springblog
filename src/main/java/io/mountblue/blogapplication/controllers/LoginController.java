package io.mountblue.blogapplication.controllers;


import io.mountblue.blogapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    private String duplicateUser = null;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/loginPage")
    public String login(){
        return "Login";
    }

    @GetMapping("/register")
    public String register(@RequestParam(value = "error",required = false)String error,Model model){
        model.addAttribute("duplicateUser",error);
        return "Registration";
    }

    @PostMapping("/registerUser")
    public String registerUser(@RequestParam(value = "name")String name,
                               @RequestParam(value = "email")String email,
                               @RequestParam(value = "password")String password,
                               Model theModel){
        return userService.registerUser(name,email,password,theModel);
    }
}
