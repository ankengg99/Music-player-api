package com.example.music_player.dao;

import com.example.music_player.model.Song;
import com.example.music_player.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SongRepo extends JpaRepository<Song,Integer> {
    @Query(value="select * from tbl_song where song_name=:name", nativeQuery=true)
    public List<Song> findSongByName(String name);
    @Modifying
    @Transactional
    @Query(value ="delete * from tbl_song where song_name=:name",
            countQuery = "select count(*) from tbl_user",nativeQuery = true)
    public void deleteByName(String name);


}
