package com.formation.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.formation.models.Post;
import com.formation.repository.PostRepository;

@Service
public class PostServices {

	@Autowired
	PostRepository postRepository;
	
	List<Post> postList =new ArrayList<>();
	
	
	public PostServices() {
	
	}
	
	public void initiation() {
	
	
	}
	
	public List<Post> getAllPost() {
		return (List<Post>) this.postRepository.findAll();
		
	}
	public Post getOnePost(Long id) {
		return this.postRepository.findById(id).get();
		
	}
	public boolean addPost(Post post) {
		return this.postRepository.save(post)!=null ? true:false;
		
	}
	
	
}
