package com.example.music_player.controller;

import com.example.music_player.dao.UserRepo;
import com.example.music_player.dto.UserDto;
import com.example.music_player.model.User;
import com.example.music_player.service.UserService;
import com.example.music_player.util.CommonValidator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value="api/v1/user")
public class UserController {
   @Autowired
    UserService service;
   @Autowired
    UserRepo repo;
   @PostMapping(value="/add-user")
   public ResponseEntity<String> addUser(@RequestBody UserDto userDto){
       ObjectMapper Obj = new ObjectMapper();
       String jsonStr="";
       try {

           jsonStr = Obj.writeValueAsString(userDto);
       } catch (IOException e) {
           e.printStackTrace();
       }
       JSONObject jsonObject=new JSONObject(jsonStr);
       JSONObject errorList=validateUser(jsonObject);
       if(errorList.isEmpty()){
         User newUser=setUser(jsonObject);
         service.saveUser(newUser);
         return new ResponseEntity<>("user saved", HttpStatus.OK);
       }
       return new ResponseEntity<>(errorList.toString(),HttpStatus.BAD_REQUEST);

   }
   @GetMapping(value="/get-user")
   public List<User> getAll(){
    return   service.getAll();
   }
   public User setUser(JSONObject jsonObject){
     User user=new User();
     user.setUserName(jsonObject.getString("username"));
     user.setUserType(jsonObject.getString("usertype"));
     user.setFirstName(jsonObject.getString("firstname"));
     user.setEmail(jsonObject.getString("email"));
     if(jsonObject.has("age")) {
         user.setAge(jsonObject.getInt("age"));
     }
       if(jsonObject.has("lastname")) {
           user.setLastName(jsonObject.getString("lastname"));
       }
     return user;
   }
   public JSONObject validateUser(JSONObject jsonObject){
       JSONObject error=new JSONObject();

       if(!jsonObject.has("username")){
           error.put("username","missing parameter");
       }
       else{
           List<User> user=repo.findByUserName(jsonObject.getString("username"));
           if(!user.isEmpty()){
             error.put("username","already exist");
             return error;
           }
       }
       if(!jsonObject.has("usertype")){
           error.put("usertype","missing parameter");
       }
       if(!jsonObject.has("firstname")){
           error.put("firstname","missing parameter");
       }
       if(jsonObject.has("email")){
          if(!CommonValidator.isValidEmail(jsonObject.getString("email"))){
              error.put("email","not valid email");
          }
       }
       else {
           error.put("email","missing parameters");
       }

       return error;
   }
}
