package com.mentalwellness.MentalWellness.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mentalwellness.MentalWellness.model.User;
public interface UserRepository extends JpaRepository<User,Long>{
    
}
