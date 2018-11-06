/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maquinadebusca.app.controller;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
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

    //incompletos: 08, 09, 10, 
    //duvidas: validação de qual usuário está logado; usar melhor o status code, qual documentação
    @Autowired
    ColetorService cs;

    // URL: http://localhost:8080/coletor/iniciar
    @GetMapping(value = "/iniciar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity inicar() {
        List<Documento> documento = cs.executar();
        if (documento != null) {
            return new ResponseEntity(documento, HttpStatus.OK);
        }
        return new ResponseEntity(new Message("erro", "Não foi possível inciar a coleta"), HttpStatus.INTERNAL_SERVER_ERROR);
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
        Host host = cs.getHost(id);
        if (host == null) {
            return new ResponseEntity(new Message("erro", "O ID informado é inválido ou não existe"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(host, HttpStatus.OK);

    }

    // URL: http://localhost:8080/coletor/documento
    @GetMapping(value = "/documento", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity listarDocumento() {
        List<Documento> documento = cs.getDocumento();
        if (documento == null) {
            return new ResponseEntity(new Message("erro", "Não existe nehum documento salvo"), HttpStatus.NOT_IMPLEMENTED);
        }
        return new ResponseEntity(documento, HttpStatus.OK);
    }

    // Request for: http://localhost:8080/coletor/documento/{id}
    @GetMapping(value = "/documento/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity listarDocumento(@PathVariable(value = "id") long id) {
        if (id <= 0) {
            return new ResponseEntity(new Message("erro", "os dados sobre o documento não foram informado corretamente"), HttpStatus.BAD_REQUEST);
        }
        Documento documento = cs.getDocumento(id);
        if (documento == null) {
            return new ResponseEntity(new Message("erro", "O ID informado é inválido ou não existe"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(documento, HttpStatus.OK);
    }

    // URL: http://localhost:8080/coletor/link
    @GetMapping(value = "/link", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity listarLink() {
        List<Link> links = cs.getLink();
        if (links == null) {
            return new ResponseEntity(new Message("erro", "Não existe nenhum link salvo"), HttpStatus.NOT_IMPLEMENTED);
        }
        return new ResponseEntity(links, HttpStatus.OK);
    }

    // Request for: http://localhost:8080/coletor/link/{id}
    @GetMapping(value = "/link/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity listarLink(@PathVariable(value = "id") long id) {
        if (id <= 0) {
            return new ResponseEntity(new Message("erro", "os dados sobre o link não foram informado corretamente"), HttpStatus.BAD_REQUEST);
        }
        Link link = cs.getLink(id);
        if (link == null) {
            return new ResponseEntity(new Message("erros", "O ID informado é inválido ou não existe"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(link, HttpStatus.OK);
    }
    //"ultimaColeta":"2018-01-12T00:00:00"

    // Request for: http://localhost:8080/coletor/link  
    @PostMapping(value = "/link", produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    public List<ResponseEntity> inserirLink(@RequestBody Sementes urls) {
        List<String> u = urls.getUrls();
        Link link = null;
        List<ResponseEntity> result = new LinkedList();
        int position = 0;
        for (String lk : u) {
            link = new Link();
            link.setUrl(lk);
            link.setUltimaColeta(urls.getUltimaColeta().get(position));
            link = cs.salvarLink(link);

            if (link != null && link.getId() > 0) {
                result.add(new ResponseEntity(link, HttpStatus.OK));
            } else {
                result.add(new ResponseEntity(new Message("erro", "os dados não foram enviados corretamente"), HttpStatus.BAD_REQUEST));
            }
            position++;
        }

        return result;
    }

    // Request for: http://localhost:8080/coletor/link  
    @PutMapping(value = "/link", produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
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

    // Request for: http://localhost:8080/coletor/link/{id} 
    @DeleteMapping(value = "/link/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity removerLink(@PathVariable(value = "id") Long id) {
        ResponseEntity resposta = null;
        if (id == null || id <= 0) {
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

    //Request for: http://localhost:8080/coletor/documento/{id}
    @DeleteMapping(value = "documento/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity removerDocumento(@PathVariable(value = "id") Long id) {
        if (id == null || id <= 0) {
            return new ResponseEntity(new Message("erro", "os dados sobre o documento não foram informado corretamente"), HttpStatus.BAD_REQUEST);
        }

        if (cs.removerDocumento(id)) {
            return new ResponseEntity(new Message("Sucesso", "Documento removido com sucesso."), HttpStatus.OK);
        }
        return new ResponseEntity(new Message("erro", "não foi possível remover o documento informado do banco de dados"), HttpStatus.NOT_ACCEPTABLE);

    }

    // Request for: http://localhost:8080/coletor/link/encontrar/{url}
    @GetMapping(value = "/link/encontrar/{url}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity encontarLink(@PathVariable(value = "url") String url) {
        List<Link> urlEncontrada = cs.encontrarLinkUrl(url);
        if (urlEncontrada == null || urlEncontrada.isEmpty()) {
            return new ResponseEntity(new Message("erro", "não foi possível encontrar a url informada"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(urlEncontrada, HttpStatus.OK);
    }

    // Request for: http://localhost:8080/coletor/host/encontrar/{host}
    @GetMapping(value = "/host/encontrar/{host}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity encontarHist(@PathVariable(value = "host") String host) {
        List<Host> hostEncontrado = cs.encontrarHost(host);
        if (hostEncontrado == null || hostEncontrado.isEmpty()) {
            return new ResponseEntity(new Message("erro", "não foi possível encontrar o host informada"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(hostEncontrado, HttpStatus.OK);
    }

    // Request for: http://localhost:8080/coletor/link/ordemAlfabetica
    @GetMapping(value = "/link/ordemAlfabetica", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity listarEmOrdemAlfabetica() {
        List<Link> listaOrdenada = cs.listarEmOrdemAlfabetica();

        if (listaOrdenada.isEmpty() || listaOrdenada == null) {
            return new ResponseEntity(new Message("erro", "não existe links salvos no banco de dados"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(listaOrdenada, HttpStatus.OK);
    }

    // Request for: http://localhost:8080/coletor/host/ordemAlfabetica
    @GetMapping(value = "/host/ordemAlfabetica", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity listarHostEmOrderAlfabetica() {
        List<Host> listaOrdenada = cs.listarEmOderAlfabetica();
        if (listaOrdenada.isEmpty() || listaOrdenada == null) {
            return new ResponseEntity(new Message("erro", "não existe hosts salvos no banco de dados"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(listaOrdenada, HttpStatus.OK);
    }

    // Request for: http://localhost:8080/coletor/link/pagina
    @GetMapping(value = "/link/pagina", produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
    public ResponseEntity listarPagina() {
        String pagina = cs.buscarPagina();
        if (pagina == null || pagina.equals("")) {
            return new ResponseEntity(new Message("erro", "não existe páginas salvas no banco de dados"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(pagina, HttpStatus.OK);
    }
}
