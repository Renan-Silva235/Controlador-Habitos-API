package com.habito_controle.User.controller;

import com.habito_controle.Security.JwtService;
import com.habito_controle.User.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;


    @Test
    @DisplayName("Deve retornar status 400 quando o dto tiver campos inválidos.")
    void return400IfArgumentsInvalids() throws Exception{
        String jsonInvalid = """
                    {
                        "name": "Re",
                        "email": "renanemail.com",
                        "password": "123"
                    }
                """;

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalid))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar o status 201 quando a dto tiver campos válidos")
    void return201IfArgumentsValid() throws Exception{
        String jsonValid = """
                    {
                        "name": "Renan",
                        "email": "renan@email.com",
                        "password": "123456"
                    }
                
                """;

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonValid))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}