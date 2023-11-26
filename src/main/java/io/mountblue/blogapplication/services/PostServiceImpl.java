package io.mountblue.blogapplication.services;

import io.mountblue.blogapplication.entity.*;
import io.mountblue.blogapplication.repository.CommentRepository;
import io.mountblue.blogapplication.repository.PostRepository;
import io.mountblue.blogapplication.repository.RolesRepository;
import io.mountblue.blogapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
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
    public String navigateNewPost(String user,Model model){
        Post newPost = new Post();
        Tag newTag = new Tag();



        model.addAttribute("username",user);
        model.addAttribute("post",newPost);
        model.addAttribute("tag",newTag);

        return "CreatePost";
    }

    public String createPost(String author,Post newPost, Tag newTag){
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
        return "redirect:/";
    }

    @Override
    public String navigateEditPost(int postId,Model model){
        Optional<Post> retrievedPostById = postRepository.findById(postId);
        if (retrievedPostById.isPresent()){
            Post oldPost = retrievedPostById.get();
            List<Tag> oldTagsList = oldPost.getTags();
            StringBuilder tags = new StringBuilder();

           for(Tag tempTag : oldTagsList){
               tags.append(tempTag.getName()).append(",");
           }
            tags.deleteCharAt(tags.length() - 1);

           model.addAttribute("postId",postId);
            model.addAttribute("tags",tags);
            model.addAttribute("post",oldPost);
        }
        else{
            //error;
        }
        return "UpdatePost";
    }

    @Override
    public String updatePost(Post updatedPost,String updatedTags,int postId,Model model){

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

            postRepository.save(updatedPost);

        return "redirect:/";
    }

    @Override
    public String deletePost(int postId) {
        if (postRepository.existsById(postId)) {
            postRepository.deleteById(postId);
        } else {
            // Handle the case where the post with the given ID is not found
        }
        return "redirect:/";
    }

    @Override
    public String sortPost(String sortBy,Model theModel, RedirectAttributes redirectAttributes) {
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
        return "redirect:/";
    }

    @Override
    public String findPaginated(int pageNo, int pageSize, String sortField, String sortDir, String authorFilter, String tagFilter,String search,Model model) {

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

                System.out.println("Role:- " + role);

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

            return "Home";
        }


}
