package com.example.music_player.controller;

import com.example.music_player.dao.SongRepo;
import com.example.music_player.dao.UserRepo;
import com.example.music_player.dto.SongDto;
import com.example.music_player.model.Song;
import com.example.music_player.model.User;
import com.example.music_player.service.SongService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(value="api/v1/song")
public class SongController {
    @Autowired
    SongService service;
    @Autowired
    SongRepo songRepo;
    @Autowired
    UserRepo userRepo;
    @PostMapping
    public ResponseEntity<String> addSong(@RequestBody SongDto songDto){
        ObjectMapper Obj = new ObjectMapper();
        String jsonStr="";
        try {

            jsonStr = Obj.writeValueAsString(songDto);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject object=new JSONObject(jsonStr);
        JSONObject error=validSong(object);
        if(error.isEmpty()){
            Song song=setSong(object);
            service.saveSong(song);
            return new ResponseEntity<>("song saved", HttpStatus.OK);
        }
        return new ResponseEntity<>(error.toString(),HttpStatus.BAD_REQUEST);
    }
@GetMapping(value="/get-song")
public List<Song> getSong(@Nullable @RequestParam String name){
     return    service.getSong(name);
}
@DeleteMapping(value="/delete-song")
public String deleteSong(@RequestParam String name){
        return service.deleteSong(name);
}
@PutMapping(value="/update-song")
public String updateSong(@RequestParam String name,@RequestBody SongDto songDto){
        return service.updateSong(name,songDto);
}
    private Song setSong(JSONObject object) {
        Song song=new Song();
        song.setSongName(object.getString("songName"));
        song.setGenre(object.getString("genre"));
        song.setTitle(object.getString("title"));
        song.setSinger(object.getString("singer"));
        User user=userRepo.findById(Integer.parseInt(object.getString("userId"))).get();
        song.setUser(user);
        Timestamp timestamp=new Timestamp(System.currentTimeMillis());
        song.setReleaseDate(timestamp);
return song;
    }

    private JSONObject validSong(JSONObject object) {
        List<Song> songList=songRepo.findSongByName(object.getString("songName"));

        JSONObject error=new JSONObject();
        if(!songList.isEmpty()){
            error.put("song","already exist");
            return error;
        }
        if(!object.has("songName")){
           error.put("songName","missing parameters");
        }
        if(!object.has("title")){
            error.put("title","missing parameters");
        }
        if(!object.has("singer")){
            error.put("singer","missing parameters");
        }
        if(!object.has("genre")){
            error.put("genre","missing parameters");
        }
        if(!object.has("userId")){
            error.put("userId","missing parameters");
        }
        else{
            User user=userRepo.findById(Integer.parseInt(object.getString("userId"))).get();
            if(!user.getUserType().equals("admin")){
                error.put("user","not valid");
            }
        }
        return error;
    }
}
