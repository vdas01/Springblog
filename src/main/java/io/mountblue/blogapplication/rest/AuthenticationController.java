package io.mountblue.blogapplication.rest;


import io.mountblue.blogapplication.entity.User;
import io.mountblue.blogapplication.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    UserRepository userRepository;

    public AuthenticationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public AuthenticationController(){

    }

    @PostMapping("/login")
    public ResponseEntity<String> login(HttpServletRequest request, @RequestParam String username, @RequestParam String password) {
        HttpSession session = request.getSession(true);

        User user = userRepository.findByName(username);

        if(user != null) {
            session.setAttribute("username", username);
            
            return new ResponseEntity<>("Login successfull", HttpStatus.OK);
        } else{
            return new ResponseEntity<>("No user found",HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // Get the HttpSession
        HttpSession session = request.getSession(false);

        // If a session exists, invalidate it
        if (session != null) {
            session.invalidate();
        }

        // Additional logout logic...

        return new ResponseEntity<>("logout succesfull", HttpStatus.OK);
    }

}
