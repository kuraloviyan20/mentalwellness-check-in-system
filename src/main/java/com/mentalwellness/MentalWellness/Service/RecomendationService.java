package com.mentalwellness.MentalWellness.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mentalwellness.MentalWellness.Repository.RecomendationRepository;
import com.mentalwellness.MentalWellness.Repository.UserRepository;
import com.mentalwellness.MentalWellness.model.Recomendation;

@Service
public class RecomendationService {
    @Autowired
    private RecomendationRepository recommendationRepo;

    @Autowired
    private UserRepository userRepository;

    public List<Recomendation> getAllRecommendations(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        
        return recommendationRepo.findByUserId(userId);
    }

    public List<Recomendation> getAllRecommendations() {
        return recommendationRepo.findAll();
    }

    public Optional<Recomendation> getRecommendationById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Recommendation ID cannot be null");
        }
        return recommendationRepo.findById(id);
    }

    @Transactional
    public Recomendation createRecommendation(Recomendation recommendation) {
        validateRecommendation(recommendation);
        return recommendationRepo.save(recommendation);
    }

    @Transactional
    public Recomendation updateRecommendation(Long id, Recomendation recommendationDetails) {
        if (id == null) {
            throw new IllegalArgumentException("Recommendation ID cannot be null");
        }

        Recomendation existingRecommendation = recommendationRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Recommendation not found with id: " + id));

        validateRecommendation(recommendationDetails);
        
        existingRecommendation.setRecomendation(recommendationDetails.getRecomendation());
        existingRecommendation.setUser(recommendationDetails.getUser());

        return recommendationRepo.save(existingRecommendation);
    }

    @Transactional
    public void deleteRecommendation(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Recommendation ID cannot be null");
        }

        if (!recommendationRepo.existsById(id)) {
            throw new IllegalArgumentException("Recommendation not found with id: " + id);
        }

        recommendationRepo.deleteById(id);
    }

    private void validateRecommendation(Recomendation recommendation) {
        if (recommendation == null) {
            throw new IllegalArgumentException("Recommendation cannot be null");
        }
        
        if (recommendation.getUser() == null) {
            throw new IllegalArgumentException("User must be specified for the recommendation");
        }
        
        if (!userRepository.existsById(recommendation.getUser().getUser_id())) {
            throw new IllegalArgumentException("Invalid user specified for the recommendation");
        }
        
        if (recommendation.getRecomendation() == null || recommendation.getRecomendation().trim().isEmpty()) {
            throw new IllegalArgumentException("Recommendation text cannot be empty");
        }
        
        if (recommendation.getRecomendation().length() < 10) {
            throw new IllegalArgumentException("Recommendation text must be at least 10 characters long");
        }
        
        if (recommendation.getRecomendation().length() > 1000) {
            throw new IllegalArgumentException("Recommendation text cannot exceed 1000 characters");
        }
    }
}
