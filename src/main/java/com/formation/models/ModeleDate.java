package com.formation.models;

import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity

public class ModeleDate implements Serializable{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	
	private Date dateSeul;
	//private Time heureSeul;
	
	private LocalDate dateLocale;
	private LocalDateTime dateLocaleTime;
	
	
	
	
	public Date getDateSeul() {
		return dateSeul;
	}
	public void setDateSeul(Date dateSeul) {
		this.dateSeul = dateSeul;
	}
	/*public Time getHeureSeul() {
		return heureSeul;
	}
	public void setHeureSeul(Time heureSeul) {
		this.heureSeul = heureSeul;
	}*/
	public LocalDate getDateLocale() {
		return dateLocale;
	}
	public void setDateLocale(LocalDate dateLocale) {
		this.dateLocale = dateLocale;
	}
	public LocalDateTime getDateLocaleTime() {
		return dateLocaleTime;
	}
	public void setDateLocaleTime(LocalDateTime dateLocaleTime) {
		this.dateLocaleTime = dateLocaleTime;
	}
	
	
	
}
