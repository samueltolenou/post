package com.formation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.formation.models.ModeleDate;
import com.formation.models.Post;

@Repository
public interface ModeleDateRepository extends JpaRepository<ModeleDate,Long> {

}
