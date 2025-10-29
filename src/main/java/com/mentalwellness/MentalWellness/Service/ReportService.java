package com.mentalwellness.MentalWellness.Service;

import java.time.LocalDate;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mentalwellness.MentalWellness.Repository.CheckInRepository;
import com.mentalwellness.MentalWellness.model.CheckIn;

@Service
public class ReportService {
    @Autowired
    private CheckInRepository checkIn_repo;

    public Map<LocalDate, Double> getAverageMoodByDateForUser(Long userId) {
        List<CheckIn> list = checkIn_repo.findByUserId(userId);
        Map<LocalDate, List<Integer>> grouped = list.stream()
                .filter(ci -> ci.getCheckin_date() != null && ci.getMood_level() != null)
                .collect(Collectors.groupingBy(ci -> ci.getCheckin_date().toLocalDate(), TreeMap::new,
                        Collectors.mapping(CheckIn::getMood_level, Collectors.toList())));

        Map<LocalDate, Double> avg = new TreeMap<>();
        for (Map.Entry<LocalDate, List<Integer>> e : grouped.entrySet()) {
            List<Integer> vals = e.getValue();
            double a = vals.stream().mapToInt(Integer::intValue).average().orElse(0.0);
            avg.put(e.getKey(), a);
        }
        return avg;
    }

    public Map<LocalDate, Double> getAverageStressByDateForUser(Long userId) {
        List<CheckIn> list = checkIn_repo.findByUserId(userId);
        Map<LocalDate, List<Integer>> grouped = list.stream()
                .filter(ci -> ci.getCheckin_date() != null && ci.getStress_level() != null)
                .collect(Collectors.groupingBy(ci -> ci.getCheckin_date().toLocalDate(), TreeMap::new,
                        Collectors.mapping(CheckIn::getStress_level, Collectors.toList())));

        Map<LocalDate, Double> avg = new TreeMap<>();
        for (Map.Entry<LocalDate, List<Integer>> e : grouped.entrySet()) {
            List<Integer> vals = e.getValue();
            double a = vals.stream().mapToInt(Integer::intValue).average().orElse(0.0);
            avg.put(e.getKey(), a);
        }
        return avg;
    }

}
