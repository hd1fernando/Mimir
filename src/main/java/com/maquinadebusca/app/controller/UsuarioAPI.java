/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maquinadebusca.app.controller;

import com.maquinadebusca.app.model.Usuario;
import com.maquinadebusca.app.service.UsuarioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.maquinadebusca.app.Message.Message;

@RestController
@RequestMapping("/usuario")
public class UsuarioAPI {

    @Autowired
    UsuarioService us;

    // URL: http://localhost:8080/usuario/remover
    @DeleteMapping(value = "/remover", produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
    public ResponseEntity removerConta() {
        //capturar o id do usuario logado e remover a conta
        return null;
    }

    // Request for: http://localhost:8080/usuario/encontrar/{nome}
    @GetMapping(value = "/encontrar/{nome}", params = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity encontrarUsuario(@PathVariable(value = "nome") String nome) {
        List<Usuario> usuarioEncontrado = us.encontarUsuarioNome(nome);
        if (usuarioEncontrado == null || usuarioEncontrado.isEmpty()) {
            return new ResponseEntity(new Message("erro", "o usuário informado não existe"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(usuarioEncontrado, HttpStatus.OK);
    }

    // URL: http://localhost:8080/usuario/ordemAlfabetica
    @GetMapping(value = "/ordemAlfabetica", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity listarEmOrdemAlfabetica() {
        List<Usuario> listaOrdenada = us.listarEmOrdemAlfabetica();
        if (listaOrdenada.isEmpty() || listaOrdenada == null) {
            return new ResponseEntity(new Message("erro", "não existe nenhum usuário cadastrado no banco de dados"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(listaOrdenada, HttpStatus.OK);
    }

}
