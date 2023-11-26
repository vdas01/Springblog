package io.mountblue.blogapplication.services;

import io.mountblue.blogapplication.entity.Roles;
import io.mountblue.blogapplication.entity.User;
import io.mountblue.blogapplication.repository.RolesRepository;
import io.mountblue.blogapplication.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    RolesRepository rolesRepository;

    public UserServiceImpl(){

    }

    public UserServiceImpl(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private  String hashPassword(String plainTextPassword) {
        String salt = BCrypt.gensalt(12);
        return BCrypt.hashpw(plainTextPassword, salt);
    }
    private static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }

    @Override
    public String registerUser(String name, String email, String password) {
       String hashedPassword = hashPassword(password);
        User user = new User(name,email,hashedPassword);
        userRepository.save(user);

        Roles role = new Roles(name,"ROLE_author");
        rolesRepository.save(role);


        return "redirect:/loginPage";
    }


}
