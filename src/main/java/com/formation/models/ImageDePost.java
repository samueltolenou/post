package com.formation.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
public class ImageDePost implements Serializable {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer idImg;
	String nomImg;
	String chemin;
 
	
	public ImageDePost() {}
	public ImageDePost(Integer idImg, String nomImg, String chemin) {

		this.idImg = idImg;
		this.nomImg = nomImg;
		this.chemin = chemin;
	}

	public Integer getIdImg() {
		return idImg;
	}

	public void setIdImg(Integer idImg) {
		this.idImg = idImg;
	}

	public String getNomImg() {
		return nomImg;
	}

	public void setNomImg(String nomImg) {
		this.nomImg = nomImg;
	}

	public String getChemin() {
		return chemin;
	}

	public void setChemin(String chemin) {
		this.chemin = chemin;
	}
	
	
	
}
