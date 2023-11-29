package io.mountblue.blogapplication.services;

import io.mountblue.blogapplication.entity.*;
import io.mountblue.blogapplication.repository.CommentRepository;
import io.mountblue.blogapplication.repository.PostRepository;
import io.mountblue.blogapplication.repository.RolesRepository;
import io.mountblue.blogapplication.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.*;

@Service
public class PostServiceImpl implements  PostService{

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepository rolesRepository;

    public PostServiceImpl(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public PostServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public PostServiceImpl(TagService tagService) {
        this.tagService = tagService;
    }

    public PostServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostServiceImpl(){}

    @Override
    public String findAllPosts(int page,String sortBy,List<Post>tempPost,Model theModel) {

        Pageable pageable;
        if(sortBy == null)
                pageable = PageRequest.of(page, 3);
        else
            pageable = PageRequest.of(page,3, Sort.by(sortBy));
        Page<Post> posts = postRepository.findAllBy(pageable);

        theModel.addAttribute("currentPage",page);
        theModel.addAttribute("totalpages",posts.getTotalPages());
        theModel.addAttribute("posts",posts);

            return "Home";
    }

    @Override
    public String getPostById(int postId, Model model){
        Optional<Post> retrievedPostById = postRepository.findById(postId);

        if (retrievedPostById.isPresent()) {
            Post post = retrievedPostById.get();
            List<Tag> tags = post.getTags();
            Comment newComment = new Comment();
            List<Comment> comments = commentRepository.findAll();

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = null;
            String role = null;
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();


                if (principal instanceof UserDetails) {
                    UserDetails userDetails = (UserDetails) principal;
                    String username = userDetails.getUsername();
                    role = userDetails.getAuthorities().toString();

                    user = userRepository.findByName(username);
                }
            }
            System.out.println(role + " by comment id");
            model.addAttribute("userRole",role);
            model.addAttribute("user",user);
            model.addAttribute("post", post);
            model.addAttribute("tags",tags);
            model.addAttribute("newComment",newComment);
            model.addAttribute("displaycomments",comments);
        }
        return "Post";
    }



    @Override
    public void navigateNewPost(String user,Model model){
        Post newPost = new Post();
        Tag newTag = new Tag();

        model.addAttribute("username",user);
        model.addAttribute("post",newPost);
        model.addAttribute("tag",newTag);
    }

    public void createPost(String author,Post newPost, Tag newTag){
        newPost.setAuthor(author);
        Map<String,Tag> tempTags = new HashMap<>();
        List<Tag> allTags = tagService.findAllTags();

        for(Tag tag:allTags){
            String name = tag.getName();
            tempTags.put(name,tag);
        }

        String[] tagsArray = newTag.getName().split(",");
        for(String tempTag: tagsArray){
           if(tempTags.containsKey(tempTag)){
               newPost.addTags(tempTags.get(tempTag));
           }
           else {
               tempTag = tempTag.trim();
               Tag tag = new Tag(tempTag);
               newPost.addTags(tag);
           }
        }

        User user = userRepository.findByName(author);
        newPost.setUser(user);
        postRepository.save(newPost);
    }

    @Override
    public void navigateEditPost(int postId,Model model){
        Optional<Post> retrievedPostById = postRepository.findById(postId);
        if (retrievedPostById.isPresent()){
            Post oldPost = retrievedPostById.get();
            List<Tag> oldTagsList = oldPost.getTags();
            StringBuilder tags = new StringBuilder();
            String author = oldPost.getAuthor();

           for(Tag tempTag : oldTagsList){
               tags.append(tempTag.getName()).append(",");
           }
            tags.deleteCharAt(tags.length() - 1);

           model.addAttribute("author",author);
           model.addAttribute("postId",postId);
            model.addAttribute("tags",tags);
            model.addAttribute("post",oldPost);
        }

    }

    @Override
    public void updatePost(String author, Post updatedPost, String updatedTags, int postId, Model model){

        Map<String,Tag> tempTags = new HashMap<>();
        List<Tag> allTags = tagService.findAllTags();

        for(Tag tag:allTags){
            String name = tag.getName();
            tempTags.put(name,tag);
        }

        String[] tagsArray = updatedTags.split(",");

        for(String tempTag: tagsArray){
            if(tempTags.containsKey(tempTag)){
                updatedPost.addTags(tempTags.get(tempTag));
            }
            else {
                tempTag = tempTag.trim();
                Tag tag = new Tag(tempTag);
                updatedPost.addTags(tag);
            }
        }
            updatedPost.setAuthor(author);
            postRepository.save(updatedPost);

    }

    @Override
    public void deletePost(int postId) {
        if (postRepository.existsById(postId)) {
            postRepository.deleteById(postId);
        }
    }

    @Override
    public void sortPost(String sortBy,Model theModel, RedirectAttributes redirectAttributes) {
        List<Post> tempPost;

        if(sortBy.equals("title")){
            tempPost = postRepository.findAllByOrderByTitleAsc();
        }else if(sortBy.equals("author")){
            tempPost = postRepository.findAllByOrderByAuthorAsc();
        }else if(sortBy.equals("publishedAtAsc")){
            tempPost =  postRepository.findAllByOrderByCreatedAtAsc();
        }else{
            tempPost = postRepository.findAllByOrderByCreatedAtDesc();
        }

        redirectAttributes.addAttribute("tempPost", tempPost);
    }

    @Override
    public void findPaginated(int pageNo, int pageSize, String sortField, String sortDir, String authorFilter, String tagFilter,String search,Model model) {

        search = (search == "") ? null : search;
        authorFilter = (authorFilter == "") ? null : authorFilter;
        tagFilter = (tagFilter == "") ? null : tagFilter;
        if (tagFilter != null && tagFilter.equals("null"))
            tagFilter = null;
        if (authorFilter != null && authorFilter.equals("null"))
            authorFilter = null;

        if (search != null && search.equals("null"))
            search = null;

        List<String> tagsList = null;
//        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
//                    Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        if (tagFilter != null) {
            String[] tagsArray = tagFilter.split(",");
            tagsList = Arrays.asList(tagsArray);
        }
        Page<Post> page = null;
        if (search != null) {
            page = postRepository.searchPosts(search, pageable);
        } else {
            page = postRepository.filterPosts(authorFilter, tagsList, sortField, sortDir, pageable);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        String role = null;
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                String username = userDetails.getUsername();
                 role = userDetails.getAuthorities().toString();

                 user = userRepository.findByName(username);


            }
        }

            List<Post> posts = page.getContent();
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalItems", page.getTotalElements());

            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

            model.addAttribute("author_filter", authorFilter);
            model.addAttribute("tag_filter", tagFilter);

            model.addAttribute("user",user);
            model.addAttribute("userRole",role);


            model.addAttribute("posts", posts);

        }

        @Override
        public List<Post> findAllPostsRest(){

         return postRepository.findAll();
        }

        @Override
        public Post findPostByIdRest(int postId){
             Post post = null;
            Optional<Post> retrievedPostById = postRepository.findById(postId);
            if(retrievedPostById.isPresent()){
                post = retrievedPostById.get();
            }

            return post;
        }

        @Override
        public String createPostRest(Post post){

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName();
                User currUser = userRepository.findByName(username);

                post.setId(0);
                post.setUser(currUser);
                post.setAuthor(username);

                postRepository.save(post);
                return "created";

            } else {
                return "Unauthorized";
            }

        }

        @Override
        public String deletePostRest(int postId){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                String role = authentication.getAuthorities().toString();
                String username = authentication.getName();

                Optional<Post> retirevedPost = postRepository.findById(postId);
                if(retirevedPost.isPresent()) {
                    Post deletePost = retirevedPost.get();

                    String author = deletePost.getAuthor();

                    if (author.equals(username) || role.equals("[ROLE_admin]")) {
                        postRepository.deleteById(postId);
                        return "deleted";
                    } else {
                       return "Unauthorized";
                    }

                } else {
                    return "Post not found";
                }
            } else {
                return "Unauthenticated";
            }

        }

    @Override
    public String updatePostRest(int postId, Post updatedPost) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String role = authentication.getAuthorities().toString();
            String username = authentication.getName();
            Optional<Post> retrivedPostById = postRepository.findById(postId);
            if (retrivedPostById.isPresent()) {
                Post oldPost = retrivedPostById.get();
                updatedPost.setId(postId);

                String author =  oldPost.getAuthor();
                if (author.equals(username) || role.equals("[ROLE_admin]")){
                    postRepository.save(updatedPost);
                    return "Edited";
                } else{
                    return "Forbidden";
                }
            } else {
                return "Post not found";
            }
        } else{
            return "Unauthorized";
        }
    }

}
