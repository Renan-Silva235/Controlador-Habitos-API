package com.habito_controle.User.controller;

import com.habito_controle.User.dto.request.UserRegisterDto;
import com.habito_controle.User.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserRegisterDto userRegisterDto){
        userService.registerUser(userRegisterDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário Cadastrado com Sucesso");
    }
}
