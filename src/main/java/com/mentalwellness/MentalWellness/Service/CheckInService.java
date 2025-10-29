package com.mentalwellness.MentalWellness.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mentalwellness.MentalWellness.Repository.CheckInRepository;
import com.mentalwellness.MentalWellness.Repository.UserRepository;
import com.mentalwellness.MentalWellness.model.CheckIn;
import com.mentalwellness.MentalWellness.model.User;

@Service
public class CheckInService {
    @Autowired
    private CheckInRepository checkIn_repo;
    @Autowired
    private UserRepository user_repo;

    public List<CheckIn> getAllCheckIns(Long id) {
        return checkIn_repo.findByUserId(id);
    }

    public List<CheckIn> getAllCheckIns() {
        return checkIn_repo.findAll();
    }

    public CheckIn createCheckIn(CheckIn checkIn) {
        if (checkIn.getUser()!=null&& checkIn.getUser().getUser_id()!=null) {
            Long uid=checkIn.getUser().getUser_id();
            User u=user_repo.findById(uid).orElse(null);
            checkIn.setUser(u);
        }
        return checkIn_repo.save(checkIn);
    }
}
