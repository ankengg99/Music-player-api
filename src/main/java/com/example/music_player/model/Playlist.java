package com.example.music_player.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="tbl_playlist")
public class Playlist {
    @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int playlistId;
   private String playlistName;
  private  Timestamp addedDate;
  private  Timestamp updatedDate;
    @JoinColumn(name="user_id")
    @ManyToOne
   private User user;
    @JoinColumn(name="song_id")
    @ManyToMany
   private List<Song> song=new ArrayList<>();
}
