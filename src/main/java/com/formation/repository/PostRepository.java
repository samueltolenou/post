package com.formation.repository;

import com.formation.models.Post;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
 
	

/*
 @Query("select p.nombreVote from Post p where p.id=:postId")
 Optional<Integer> nombreVoteById(@Param("postId") long id);
 
 @Query(nativeQuery = true, value = "update post set nombre_vote= "+4+" where id=:postId")
 void incrementVote(@Param("postId") long id);
*/
	
	
}
