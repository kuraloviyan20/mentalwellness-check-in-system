package com.mentalwellness.MentalWellness.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mentalwellness.MentalWellness.Repository.CheckInRepository;
import com.mentalwellness.MentalWellness.Repository.RecomendationRepository;
import com.mentalwellness.MentalWellness.Repository.UserRepository;
import com.mentalwellness.MentalWellness.model.CheckIn;
import com.mentalwellness.MentalWellness.model.Recomendation;
import com.mentalwellness.MentalWellness.model.User;

@Service
public class TrendRecommendationService {
    @Autowired
    private CheckInRepository checkInRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecomendationRepository recomendationRepository;

    @Transactional
    public Recomendation generateForUser(Long userId) {
        if (userId == null) throw new IllegalArgumentException("User ID cannot be null");
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        LocalDateTime since = LocalDateTime.now().minusDays(7);
        List<CheckIn> recent = checkInRepository.findByUserIdSince(userId, since);
        if (recent.isEmpty()) throw new IllegalArgumentException("No recent check-ins to generate a recommendation");

        double avgMood = recent.stream().filter(c -> c.getMood_level()!=null).mapToInt(CheckIn::getMood_level).average().orElse(Double.NaN);
        double avgStress = recent.stream().filter(c -> c.getStress_level()!=null).mapToInt(CheckIn::getStress_level).average().orElse(Double.NaN);
        double avgSleep = recent.stream().filter(c -> c.getSleep_hours()!=null).mapToDouble(c -> c.getSleep_hours()).average().orElse(Double.NaN);

        Integer firstMood = recent.stream().filter(c -> c.getMood_level()!=null).findFirst().map(CheckIn::getMood_level).orElse(null);
        Integer lastMood = null;
        for (int i = recent.size()-1; i>=0; i--) { if (recent.get(i).getMood_level()!=null) { lastMood = recent.get(i).getMood_level(); break; } }
        Integer firstStress = recent.stream().filter(c -> c.getStress_level()!=null).findFirst().map(CheckIn::getStress_level).orElse(null);
        Integer lastStress = null;
        for (int i = recent.size()-1; i>=0; i--) { if (recent.get(i).getStress_level()!=null) { lastStress = recent.get(i).getStress_level(); break; } }

        StringBuilder sb = new StringBuilder();
        String name = user.getName()!=null ? user.getName() : "User";
        sb.append(String.format(Locale.ENGLISH, "%s, here is your last 7 days wellness summary. ", name));
        if (!Double.isNaN(avgMood)) sb.append(String.format(Locale.ENGLISH, "Average mood: %.1f/10. ", avgMood));
        if (!Double.isNaN(avgStress)) sb.append(String.format(Locale.ENGLISH, "Average stress: %.1f/10. ", avgStress));
        if (!Double.isNaN(avgSleep)) sb.append(String.format(Locale.ENGLISH, "Average sleep: %.1f hrs. ", avgSleep));

        if (firstMood!=null && lastMood!=null) {
            int delta = lastMood - firstMood;
            if (delta <= -2) sb.append("Mood trend decreased. Consider scheduling some relaxing activities and reaching out to a friend or counselor. ");
            else if (delta >= 2) sb.append("Mood trend improved. Keep maintaining activities that help you feel better. ");
        }
        if (firstStress!=null && lastStress!=null) {
            int delta = lastStress - firstStress;
            if (delta >= 2) sb.append("Stress has increased. Try short mindfulness breaks, limit screen time before bed, and prioritize tasks. ");
            else if (delta <= -2) sb.append("Stress has reduced. Continue with your effective coping strategies. ");
        }
        if (!Double.isNaN(avgSleep)) {
            if (avgSleep < 7.0) sb.append("Sleep is below 7 hours on average. Aim for consistent bedtimes and a wind-down routine. ");
            else if (avgSleep >= 7.0 && avgSleep < 8.5) sb.append("Sleep duration looks healthy. Keep a consistent schedule. ");
            else sb.append("You sleep quite a lot. Ensure daytime activity and consider reducing long naps. ");
        }
        if (sb.length() < 40) sb.append("Keep tracking daily. Small consistent habits make a big difference.");

        Recomendation rec = new Recomendation();
        rec.setUser(user);
        rec.setRecomendation(sb.toString().trim());
        return recomendationRepository.save(rec);
    }
}
