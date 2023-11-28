package io.mountblue.blogapplication.security;


import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        // define query to retrieve a user by username
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select username, password,isactive from users where username=?");

        // define query to retrieve the authorities/roles by username
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select username, role from roles where username=?");

        return jdbcUserDetailsManager;
    }
//   @Bean
//    public InMemoryUserDetailsManager userDetailsManager(){
//       UserDetails vishal = User.builder().username("vishal").password("{noop}test123").roles("EMPLOYEE").build();
//
//       return  new InMemoryUserDetailsManager(vishal);
//   }


   @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception{
       http.authorizeHttpRequests(configurer ->
               configurer
                       .requestMatchers("/").hasAnyRole("author","admin")
                       .requestMatchers("/register","/registerUser","/css/**").permitAll()
                       .anyRequest().authenticated()
       ).formLogin(form ->
               form
                       .loginPage("/loginPage")
                       .loginProcessingUrl("/authenticateUser")
                       .permitAll()
       ).logout(logout -> logout.permitAll()
               )
               .exceptionHandling(configurer ->
                       configurer.accessDeniedPage("/access-denied")
               );

       http.httpBasic(Customizer.withDefaults());

       http.csrf(csrf -> csrf.disable());

       return http.build();
   }
}
