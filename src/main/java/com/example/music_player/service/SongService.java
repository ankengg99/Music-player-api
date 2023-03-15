package com.example.music_player.service;

import com.example.music_player.dao.SongRepo;
import com.example.music_player.dto.SongDto;
import com.example.music_player.model.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class SongService {
    @Autowired
    SongRepo songRepo;

    public List<Song> getSong(String name) {
        List<Song> songList=songRepo.findSongByName(name);
        if(songList.isEmpty()){

            return null;
        }
     if(name==null){
         return songRepo.findAll();
     }
     return songList;
    }

    public void saveSong(Song song) {
        songRepo.save(song);
    }

    public String deleteSong(String name) {
        List<Song> songList=songRepo.findSongByName(name);
        if(songList.isEmpty()){
            return "song does not exist";
        }
         songRepo.deleteByName(name);
        return "deleted";

    }

    public String updateSong(String name, SongDto songDto) {
        List<Song> songList=songRepo.findSongByName(name);
        if(songList.isEmpty()){
            return "song does not exist";
        }
        Song song=songList.get(0);
        song.setSongName(songDto.getSongName());
        song.setSinger(songDto.getSinger());
        song.setTitle(songDto.getTitle());
        song.setGenre(songDto.getGenre());
        Timestamp timestamp=new Timestamp(System.currentTimeMillis());
        song.setUpdatedDate(timestamp);
        songRepo.save(song);
        return "song updated";
    }
}
