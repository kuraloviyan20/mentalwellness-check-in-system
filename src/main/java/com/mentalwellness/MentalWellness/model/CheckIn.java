package com.mentalwellness.MentalWellness.model;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class CheckIn {
        
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long checkin_id;
    
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    
    private Integer mood_level;
    private Integer stress_level;
    private Float sleep_hours;
    private String notes;

    @CreationTimestamp
    @Column(name = "checkin_date", nullable = false)
    private LocalDateTime checkin_date;
  
}
