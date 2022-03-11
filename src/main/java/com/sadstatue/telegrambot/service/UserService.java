package com.sadstatue.telegrambot.service;

import com.sadstatue.telegrambot.persistence.repository.UserRepo;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }



}
