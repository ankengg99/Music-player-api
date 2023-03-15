package com.example.music_player.dao;

import com.example.music_player.model.Playlist;
import com.example.music_player.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepo extends JpaRepository<Playlist,Integer> {
    @Query(value="select * from tbl_playlist where playlist_name=:name ", nativeQuery=true)
    public List<Playlist> findByName(String name);
}
