package com.mentalwellness.MentalWellness.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mentalwellness.MentalWellness.Repository.RecomendationRepository;
import com.mentalwellness.MentalWellness.Repository.UserRepository;
import com.mentalwellness.MentalWellness.model.User;

@Component
public class AutoRecommendationScheduler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrendRecommendationService trendRecommendationService;

    @Autowired
    private RecomendationRepository recomendationRepository;

    // Run every day at 18:00 server time
    @Scheduled(cron = "0 0 18 * * *")
    public void generateDailyRecommendations() {
        LocalDateTime since = LocalDateTime.now().minusDays(1);
        Timestamp sinceTs = Timestamp.valueOf(since);

        List<User> users = userRepository.findAll();
        for (User u : users) {
            Long uid = u.getUser_id();
            if (uid == null) continue;
            long alreadyToday = recomendationRepository.countForUserSince(uid, sinceTs);
            if (alreadyToday > 0) continue; // avoid duplicates for today
            try {
                trendRecommendationService.generateForUser(uid);
            } catch (IllegalArgumentException ex) {
                // no recent check-ins etc. -> skip politely
            } catch (Exception ex) {
                // log or skip; for now silently skip to keep schedule robust
            }
        }
    }
}
