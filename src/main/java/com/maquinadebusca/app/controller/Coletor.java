/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maquinadebusca.app.controller;

import com.maquinadebusca.app.Message.Message;
import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.Host;
import com.maquinadebusca.app.model.Link;
import com.maquinadebusca.app.sementes.Sementes;
import com.maquinadebusca.app.service.ColetorService;
import java.util.LinkedList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/coletor") // URL: http://localhost:8080/coletor
public class Coletor {

    @Autowired
    ColetorService cs;

    // URL: http://localhost:8080/coletor/iniciar
    @GetMapping(value = "/iniciar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity inicar() {
        List<Documento> documento = cs.executar();
        if (documento != null) {
            return new ResponseEntity(documento, HttpStatus.OK);
        }
        return new ResponseEntity(new Message("erro", "Não foi possível inciaar a coleta"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // URL: http://localhost:8080/coletor/host
    @GetMapping(value = "/host", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity listarHost() {
        List<Host> host = cs.getHost();
        if (host != null) {
            return new ResponseEntity(host, HttpStatus.OK);
        }
        return new ResponseEntity(new Message("erro", "Não foi possível iniciar a coleta"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Request for: http://localhost:8080/coletor/host/{id}
    @GetMapping(value = "/host/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity listarHost(@PathVariable(value = "id") long id) {
        if (id <= 0) {
            return new ResponseEntity(new Message("erro", "os dados sobre o link não foram informado corretamente"), HttpStatus.BAD_REQUEST);
        }
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

    //lista 6  recebe várias urls mas é incapaz de trata-las devido ao tipo em lk
    // Request for: http://localhost:8080/coletor/link  
    @PostMapping(value = "/link", produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
    public List<ResponseEntity> inserirLink(@RequestBody Sementes urls) {
        List<String> u = urls.getUrls();
        Link link = null;
        List<ResponseEntity> result = new LinkedList();

        for (String lk : u) {
            link = new Link();
            link.setUrl(lk);
            link = cs.salvarLink(link);

            if (link != null && link.getId() > 0) {
                result.add(new ResponseEntity(link, HttpStatus.OK));
            } else {
                result.add(new ResponseEntity(link, HttpStatus.BAD_REQUEST));
            }

        }

        return result;
    }

    //lista 7
    // Request for: http://localhost:8080/coletor/link  
//    @PostMapping(value = "/link", produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
//    public ResponseEntity inserirLink(@RequestBody @Valid Link link, BindingResult resultado) {
//        ResponseEntity resposta = null;
//        
//        if (resultado.hasErrors()) {
//            resposta = new ResponseEntity(new Message("erro", "Os dados sobre o link não foram informados corretamente"), HttpStatus.INTERNAL_SERVER_ERROR);
//        } else {
//            link = cs.salvarLink(link);
//            if (link != null && link.getId() > 0) {
//                resposta = new ResponseEntity(link, HttpStatus.OK);
//            } else {
//                resposta = new ResponseEntity(new Message("erro", "Não foi possível inserir o link informado no banco de dados"), HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        }
//        return resposta;
//    }
    
    //lista 8
    // Request for: http://localhost:8080/coletor/link  
    @PutMapping(value = "/link", produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
    public ResponseEntity atualizarLink(@RequestBody @Valid Link link, BindingResult resultado) {
        ResponseEntity resposta = null;
        if (resultado.hasErrors()) {
            resposta = new ResponseEntity(new Message("erro", "Os dados sobre o link não foram informados corretamente"), HttpStatus.BAD_REQUEST);
        } else {
            link = cs.salvarLink(link);
            if (link != null && link.getId() > 0) {
                resposta = new ResponseEntity(link, HttpStatus.OK);
            } else {
                resposta = new ResponseEntity(new Message("erro", "Não foi possível inserir o link informado no banco de dados"), HttpStatus.NOT_ACCEPTABLE);
            }
        }
        return resposta;
    }


    //remoção atraves do identificado ID
    // Request for: http://localhost:8080/coletor/link/{id} 
    @DeleteMapping(value = "/link/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity removerLink(@PathVariable(value = "id")  Long id) {
        ResponseEntity resposta = null;
        if (id != null && id <= 0) {
            resposta = new ResponseEntity(new Message("erro", "os dados sobre o link não foram informado corretamente"), HttpStatus.BAD_REQUEST);
        } else {
            boolean respo = cs.removerLink(id);
            if (respo) {
                resposta = new ResponseEntity(new Message("sucesso", "link removido com sucesso"), HttpStatus.OK);
            } else {
                resposta = new ResponseEntity(new Message("erro", "não foi possível remover o link informado do banco de dados"), HttpStatus.NOT_ACCEPTABLE);
            }
        }
        return resposta;
    }

    //lista 10
    // Request for: http://localhost:8080/coletor/encontar/{id}
    @GetMapping(value = "/encontar/{url}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity encontarLink(@PathVariable(value = "url") String url) {
        return new ResponseEntity(cs.encontrarLinkUrl(url), HttpStatus.OK);
    }

}
