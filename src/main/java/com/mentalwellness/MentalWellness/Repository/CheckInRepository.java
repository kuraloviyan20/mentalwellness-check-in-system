package com.mentalwellness.MentalWellness.Repository;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mentalwellness.MentalWellness.model.CheckIn;

public interface CheckInRepository extends JpaRepository<CheckIn,Long>{

	@Query("SELECT c FROM CheckIn c WHERE c.user.user_id=:userId")
	List<CheckIn> findByUserId(@Param("userId") Long userId);

	@Query("SELECT c FROM CheckIn c WHERE c.user.user_id=:userId AND c.checkin_date >= :since ORDER BY c.checkin_date ASC")
	List<CheckIn> findByUserIdSince(@Param("userId") Long userId, @Param("since") LocalDateTime since);

}
