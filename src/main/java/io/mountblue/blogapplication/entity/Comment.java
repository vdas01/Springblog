package io.mountblue.blogapplication.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;

@Entity
@Table(name = "comment" )
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "comment")
    private  String comment;

    @Column(name = "createdAt",updatable = false)
    @CreationTimestamp
    private String createdAt;


    @Column(name = "updatedAt")
    @UpdateTimestamp
    private String updatedAt;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment(){

    }

    public Comment(String name, String email, String comment) {
        this.name = name;
        this.email = email;
        this.comment = comment;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    //add conveience method

}