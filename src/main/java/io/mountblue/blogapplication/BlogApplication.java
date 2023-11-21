package io.mountblue.blogapplication;

import io.mountblue.blogapplication.dao.AppDAO;
import io.mountblue.blogapplication.entity.Post;
import io.mountblue.blogapplication.entity.Tag;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}
//	@Bean
//	public CommandLineRunner commandLineRunner(AppDAO appDAO){
//		return runner -> {
////			createPost(appDAO);
//		};

}

//	private void createPost(AppDAO appDAO) {
//		Post post = new Post(
//			"Ice Dargon", "It is a awesome blog","It is awesome blog and testing it",
//				"Vishal das",true);
//
//		//adding new tag
//		Tag tag1 = new Tag("First Post");
//		Tag tag2 = new Tag("Testing post");
//		post.addTags(tag1);
//		post.addTags(tag2);
//
//		appDAO.save(post);
//	}


