package com.formation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.formation.models.ModeleDate;
import com.formation.repository.ModeleDateRepository;

import io.swagger.annotations.ApiOperation;

@RestController
@ApiOperation(value = "api pour tester les dates ")
public class ModeleDateController {
	
	@Autowired
	ModeleDateRepository modeleDateRepository ;
	
	
	@PostMapping("/postDate")
	ResponseEntity<?> postDate(@RequestBody ModeleDate model ){
	 try {
		model = this.modeleDateRepository.save(model);
		
		return ResponseEntity.ok().body(model);
	} catch (Exception e) {
	   	
		return ResponseEntity.ok().body(e.getLocalizedMessage());
		
	}
	 	
	}

}
