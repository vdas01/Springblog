package io.mountblue.blogapplication.entity;

import jakarta.persistence.*;
import org.hibernate.engine.internal.Cascade;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "isactive")
    private boolean isActive = true;


    //one to many :- post
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Post> posts;

    //one to many:- comment

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
    private List<Comment> comments;

    public User(){

    }
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    //add convenience method
    public void addPosts(Post post){
        if(posts == null)
            posts = new ArrayList<>();
        posts.add(post);
    }

    public void addComments(Comment comment){
        if(comments == null)
            comments = new ArrayList<>();
        comments.add(comment);
    }

}

