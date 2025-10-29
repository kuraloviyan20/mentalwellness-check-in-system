package com.mentalwellness.MentalWellness.model;

import java.sql.Timestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Entity
@Data
public class Recomendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rec_id;
    
    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @NotBlank(message = "Recommendation text is required")
    @Size(min = 10, max = 1000, message = "Recommendation must be between 10 and 1000 characters")
    @Column(length = 1000, nullable = false)
    private String recomendation;
    
    @Column(nullable = false, updatable = false)
    private Timestamp created_at;
    
    @PrePersist
    protected void onCreate() {
        if (created_at == null) {
            created_at = new Timestamp(System.currentTimeMillis());
        }
    }
}
