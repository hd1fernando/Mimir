package com.maquinadebusca.app.controller;

import com.maquinadebusca.app.Message.Message;
import com.maquinadebusca.app.service.IndexadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping ("/indexador") // URL: http://localhost:8080/indexador
public class Indexador {

  @Autowired
  IndexadorService is;

  // URL: http://localhost:8080/indexador/indice
  @PostMapping (value = "/indice", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity criarIndice () {
    boolean confirmacao = is.criarIndice ();
    ResponseEntity resp;

    if (confirmacao) {
      resp = new ResponseEntity (new Message ("sucesso", "o índice invertido foi criado com sucesso"), HttpStatus.CREATED);
    } else {
      resp = new ResponseEntity (new Message ("erro", "o índice invertido não pode ser criado"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return resp;
  }

  // URL: http://localhost:8080/indexador/documento
  @GetMapping (value = "/documento", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity getDocumento () {
    return new ResponseEntity (is.getDocumentos (), HttpStatus.CREATED);
  }

}
