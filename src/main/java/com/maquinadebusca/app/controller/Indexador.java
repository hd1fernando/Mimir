/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maquinadebusca.app.controller;

import com.maquinadebusca.app.Message.Message;
import com.maquinadebusca.app.service.IndexadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/indexador") // URL: http://localhost:8080/indexador
public class Indexador {

    @Autowired
    IndexadorService is;

    // URL: http://localhost:8080/indexador/indice
    @PostMapping(value = "/indice", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity criarIndice() {
        boolean confirmacao = is.criarIndice();
        if (confirmacao) {
            return new ResponseEntity(new Message("sucesso", "indice invertido criado com sucesso"), HttpStatus.CREATED);
        }
        return new ResponseEntity(new Message("erro", "indice invertido não pode ser criado"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // URL: http://localhost:8080/indexador/documento
    @GetMapping(value = "/documento", produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
    public ResponseEntity getDocumento() {
        return new ResponseEntity(is.getDocumentos(), HttpStatus.CREATED);
    }

}
