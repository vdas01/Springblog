package io.mountblue.blogapplication.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotEmpty(message = "Title should not be empty")
    @Column(name = "title")
    private String title;

    @NotEmpty(message = "Excerpt should not be empty")
    @Column(name = "excerpt",columnDefinition = "TEXT")
    private String excerpt;

    @NotEmpty(message = "Content should not be empty")
    @Column(name = "content",columnDefinition = "TEXT")
    private String content;

    @NotEmpty(message = "Author name cannot be empty")
    @Column(name = "author")
    private String author;

    @Column(name = "published_at")
    @CreationTimestamp
    private Date publishedAt;

    @Column(name = "is_published",updatable = false)
    private boolean isPublished;

    @Column(name="created_at",updatable = false)
    @CreationTimestamp
    private Date createdAt;

    @Column(name="updated_at")
    @UpdateTimestamp
    private Date updatedAt;

    @ManyToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinTable(name="post_tag",joinColumns = @JoinColumn(name = "post_id"),inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<Comment> comments;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE,CascadeType.DETACH})
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //The mappedBy attribute is used to specify the field in the Comment entity that owns the relationship.
    //In this case, it's the post field in the Comment entity.
    public Post(){

    }

    public Post(String title, String excerpt, String content, String author, boolean isPublished) {
        this.title = title;
        this.excerpt = excerpt;
        this.content = content;
        this.author = author;
        this.isPublished = isPublished;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }



    //add convience method to add tags
    public void addTags(Tag theTag){
        if(tags == null)
            tags = new ArrayList<>();
        tags.add(theTag);
    }

    public void addComment(Comment newComment){
        if(comments == null)
            comments = new ArrayList<>();
        comments.add(newComment);
    }

    //clear tags
    public void clearTags(){
        tags = null;
    }

    //get tags
    public List<Tag> getTags(){
        return tags;
    }
}
