package com.mentalwellness.MentalWellness.Repository;

import java.util.List;
import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mentalwellness.MentalWellness.model.Recomendation;

public interface RecomendationRepository extends JpaRepository<Recomendation,Long>{

	@Query("SELECT r FROM Recomendation r WHERE r.user.user_id = :userId")
	List<Recomendation> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(r) FROM Recomendation r WHERE r.user.user_id = :userId AND r.created_at >= :since")
    long countForUserSince(@Param("userId") Long userId, @Param("since") Timestamp since);
    
}
