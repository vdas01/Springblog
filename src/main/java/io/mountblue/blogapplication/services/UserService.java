package io.mountblue.blogapplication.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserService{

    String registerUser(String name,String email,String password);


}
