package com.formation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import com.formation.services.PostServices;
import com.formation.models.*;
import com.formation.payload.Reponse;
import com.formation.repository.PostRepository;


@RestController
@RequestMapping("/post")
public class PostController {

	@Autowired
	public PostServices postService;
	@Autowired 
	PostRepository postRepository;
	
	@GetMapping("all")
	public List<Post> getAll(){
	
	   this.postService.initiation();
	   return this.postService.getAllPost();
	}
	
	
	@GetMapping("one/{id}")
	public ResponseEntity<Reponse> getOne(@PathVariable(name = "id") Long id) {
		try {
			
			Optional<Post> p = this.postRepository.findById(id);
			Reponse rep = new Reponse(1,"",p.get()); 
			
			return new ResponseEntity<Reponse>(rep,HttpStatus.OK);
		}catch (Exception e) {
			
			Reponse rep = new Reponse(0,e.getMessage(),null); 
			return new ResponseEntity<Reponse>(rep,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
	}
	
	
	@PostMapping("add")
	public ResponseEntity<?> addOne(@RequestBody Post newPost) {

		if(newPost.getTitre()==null){
			return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
		}
			if(this.postService.addPost(newPost))
			{
				Reponse rep = new Reponse(1," post bien enregistrer",newPost); 
				return new ResponseEntity<Reponse>(rep,HttpStatus.OK);
			}
			
			Reponse rep = new Reponse(0,"erreur lors de l'enregistrement",null); 
			return new ResponseEntity<Reponse>(rep,HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@GetMapping("like")
	public ResponseEntity<Reponse> addLike(
			@RequestParam(name = "postId")
					Long id) {
		try {
			
			/*
			this.postRepository.incrementVote(id);
			Optional<Integer> nbVote = this.postRepository.nombreVoteById(id);
			Reponse rep = new Reponse(1,"",nbVote.get()); 
			*/
			
			Optional<Post> lePost = this.postRepository.findById(id);
			int nbV = lePost.get().getNombreVote();
			lePost.get().setNombreVote(nbV+1);
			
			this.postRepository.save(lePost.get());
			
			Reponse rep = new Reponse(1,"",lePost.get().getNombreVote());
			
			return new ResponseEntity<Reponse>(rep,HttpStatus.OK);
		}catch (Exception e) {
			
			Reponse rep = new Reponse(0,e.getMessage(),null); 
			return new ResponseEntity<Reponse>(rep,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
	}

	
	
}


