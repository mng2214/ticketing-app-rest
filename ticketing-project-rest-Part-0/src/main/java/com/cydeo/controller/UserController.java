package com.cydeo.controller;

import com.cydeo.dto.UserDTO;
import com.cydeo.entity.ResponseWrapper;
import com.cydeo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ResponseWrapper> getAllUsers() {
        List<UserDTO> listOfUsers = userService.listAllUsers();
        return ResponseEntity.ok(new ResponseWrapper("Users successfully retrieved", listOfUsers, HttpStatus.OK));
    }

    @GetMapping("/{userName}")
    public ResponseEntity<ResponseWrapper> getUser(@PathVariable("userName") String userName) {
        UserDTO foundByUserName = userService.findByUserName(userName);
        return ResponseEntity.ok(new ResponseWrapper("User found", foundByUserName, HttpStatus.FOUND));
    }

    @PostMapping()
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody UserDTO userDTO) {
        userService.save(userDTO);
        return ResponseEntity.ok(new ResponseWrapper("User added successfully", userDTO, HttpStatus.CREATED));
    }

    @PutMapping()
    public ResponseEntity<ResponseWrapper> updateUser(@RequestBody UserDTO userDTO) {
        userService.update(userDTO);
        return ResponseEntity.ok(new ResponseWrapper("User updated successfully", HttpStatus.OK));
    }

    @DeleteMapping("/{userName}")
    public ResponseEntity<ResponseWrapper> deleteUser(@PathVariable("userName") String userName) {
        userService.deleteByUserName(userName);
        return ResponseEntity.ok(new ResponseWrapper("User deleted successfully", HttpStatus.OK));
    }


}
