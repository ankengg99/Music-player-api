package com.example.music_player.dao;

import com.example.music_player.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {
    @Query(value="select * from tbl_user where user_name=:username ", nativeQuery=true)
    public List<User> findByUserName(String username);
}
