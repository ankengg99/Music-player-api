package com.example.music_player.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="tbl_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int userId;

    private String userName;
   private String userType;
   private String firstName;
    private String lastName;
   private String email;
   private int age;

}
