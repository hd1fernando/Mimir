/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maquinadebusca.app.controller;

import com.maquinadebusca.app.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
public class UsuarioAPI {

    @Autowired
    UsuarioService us;

    // URL: http://localhost:8080/usuario/remover
    @DeleteMapping(value = "/remover", produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
    public ResponseEntity removerConta(){
        //capturar o id do usuario logado e remover a conta
        return null;
    }
}
