package com.formation.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import lombok.Builder.Default;
import lombok.Data;




@Entity
@Table(name = "post")
public class Post implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	String titre;

	String body;
	
	
	
	int nombreVote;
	
//	@ManyToOne(optional = true)
//	User auteur;
//
//	@ManyToOne(optional = true)
//	ImageDePost img;

	public Post(){
	}

	public Post(Long id, String titre, String body, int nombreVote) {
		this.id = id;
		this.titre = titre;
		this.body = body;
		this.nombreVote = nombreVote;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getNombreVote() {
		return nombreVote;
	}

	public void setNombreVote(int nombreVote) {
		this.nombreVote = nombreVote;
	}
}
