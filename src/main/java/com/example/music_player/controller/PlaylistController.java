package com.example.music_player.controller;

import com.example.music_player.dao.PlaylistRepo;
import com.example.music_player.dao.UserRepo;
import com.example.music_player.dto.PlaylistDto;
import com.example.music_player.model.Playlist;
import com.example.music_player.model.User;
import com.example.music_player.service.PlaylistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(value="api/v1/playlist")
public class PlaylistController {
    @Autowired
    PlaylistService playlistService;
    @Autowired
    PlaylistRepo repo;
    @Autowired
    UserRepo userRepo;
    @PostMapping
    public ResponseEntity<String> addPlaylist(@RequestBody PlaylistDto playlistDto){
        ObjectMapper Obj = new ObjectMapper();
        String jsonStr="";
        try {

            jsonStr = Obj.writeValueAsString(playlistDto);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject object=new JSONObject(jsonStr);
       JSONObject error=validPlaylist(object);
       if(error.isEmpty()){
           Playlist playlist=setPlaylist(object);
           playlistService.savePlaylist(playlist);
           return new ResponseEntity<>("playlist added", HttpStatus.OK);
       }
       return new ResponseEntity<>(error.toString(),HttpStatus.BAD_REQUEST);
    }
    @GetMapping(value="/get-playlist/id")
    public ResponseEntity<String> getPlaylistById(@RequestParam int id){
        Playlist playlist= playlistService.getBYId(id);
        if (playlist==null){
            return new ResponseEntity<>("playlist not exist",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(playlist.toString(),HttpStatus.OK);
    }
    @GetMapping(value="/get-playlist")
    public List<Playlist> getPlaylist(){
       return playlistService.getAllplaylist();
    }
    @DeleteMapping(value="/delete-playlist/id")
    public String deletePlaylist(@RequestParam int id){
        return  playlistService.deleteById(id);
    }
     @PutMapping(value="/update-playlist/id")
     public String updatePlaylist(@RequestParam int id,@RequestBody PlaylistDto Playlist){
       return playlistService.updateById(id,Playlist);
     }
    @PutMapping(value="/add-song")
    public String addSong(@RequestParam String songName, @RequestParam int playlistId){
       return playlistService.addSong(songName,playlistId);
    }
    @PutMapping(value="/delete/song")
    public String deleteSong(@RequestParam String songName, @RequestParam int playlistId){
       return playlistService.deleteSong(songName,playlistId);
    }
    public JSONObject validPlaylist(JSONObject object){
        JSONObject error=new JSONObject();
        List<Playlist> playlistList=repo.findByName(object.getString("playlistName"));
        if(!playlistList.isEmpty()){
            error.put("playlist","already exist");
            return error;
        }
        if(!object.has("playlistName")){
            error.put("playlistName","missing parameters");
        }
        if(!object.has("userId")){
            error.put("userId","missing parameters");
        }
        else{
            User user=userRepo.findById(Integer.parseInt(object.getString("userId"))).get();
            if(!user.getUserType().equals("normal")){
                error.put("user","user is admin required normal");
            }
        }
        return error;
    }
    public Playlist setPlaylist(JSONObject object){
        Playlist playlist=new Playlist();
        playlist.setPlaylistName(object.getString("playlistName"));
        User user=userRepo.findById(Integer.parseInt(object.getString("userId"))).get();
        playlist.setUser(user);
        Timestamp createdate=new Timestamp(System.currentTimeMillis());
        playlist.setAddedDate(createdate);
        return playlist;
    }
}
