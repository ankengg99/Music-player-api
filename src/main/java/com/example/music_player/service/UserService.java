package com.example.music_player.service;

import com.example.music_player.dao.UserRepo;
import com.example.music_player.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;
    public void saveUser(User newUser) {
      userRepo.save(newUser);
    }

    public List<User> getAll() {
      return  userRepo.findAll();
    }
}
