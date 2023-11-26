package io.mountblue.blogapplication.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

public interface UserService{

    String registerUser(String name, String email, String password, Model theModel);


}
