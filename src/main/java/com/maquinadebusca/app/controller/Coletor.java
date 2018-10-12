/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maquinadebusca.app.controller;

import com.maquinadebusca.app.Message.Message;
import com.maquinadebusca.app.model.Link;
import com.maquinadebusca.app.sementes.Sementes;
import com.maquinadebusca.app.service.ColetorService;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/coletor") // URL: http://localhost:8080/coletor
public class Coletor {

    @Autowired
    ColetorService cs;

    // URL: http://localhost:8080/coletor/iniciar
    @GetMapping(value = "/iniciar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity inicar() {
        return new ResponseEntity(cs.executar(), HttpStatus.OK);
    }

    // URL: http://localhost:8080/coletor/host
    @GetMapping(value = "/host", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity listarHost() {
        return new ResponseEntity(cs.getHost(), HttpStatus.OK);
    }

    // Request for: http://localhost:8080/coletor/host/{id}
    @GetMapping(value = "/host/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity listarHost(@PathVariable(value = "id") long id) {
        return new ResponseEntity(cs.getHost(id), HttpStatus.OK);
    }

    // URL: http://localhost:8080/coletor/documento
    @GetMapping(value = "/documento", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity listarDocumento() {
        return new ResponseEntity(cs.getDocumento(), HttpStatus.OK);
    }

    // Request for: http://localhost:8080/coletor/documento/{id}
    @GetMapping(value = "/documento/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity listarDocumento(@PathVariable(value = "id") long id) {
        return new ResponseEntity(cs.getDocumento(id), HttpStatus.OK);
    }

    // URL: http://localhost:8080/coletor/link
    @GetMapping(value = "/link", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity listarLink() {
        return new ResponseEntity(cs.getLink(), HttpStatus.OK);
    }

    // Request for: http://localhost:8080/coletor/link/{id}
    @GetMapping(value = "/link/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity listarLink(@PathVariable(value = "id") long id) {
        return new ResponseEntity(cs.getLink(id), HttpStatus.OK);
    }

    // Request for: http://localhost:8080/coletor/link  
    @PostMapping(value = "/link", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity inserirLink(@RequestBody @Valid Link url, BindingResult resultado) {
        ResponseEntity resposta = null;
        if (resultado.hasErrors()) {
            
            resposta = new ResponseEntity(new Message("erro", "os dados sobre o link  não foram informados corretamente"), HttpStatus.BAD_REQUEST);
        } else {
            url = cs.salvarLink(url);
            if ((url != null) && (url.getId() > 0)) {
                resposta = new ResponseEntity(url, HttpStatus.OK);
            } else {
                resposta = new ResponseEntity(new Message("erro", "não foi possível inserir o link informado no banco de dados"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return resposta;
    }
}
