package com.maquinadebusca.app.controller;

import com.maquinadebusca.app.Message.Message;
import com.maquinadebusca.app.service.UsuarioService;
import com.maquinadebusca.app.model.Usuario;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
public class UsuarioAPI {
//mapped superclass

    @Autowired
    UsuarioService us;

    // URL: http://localhost:8080/usuario/cadastrar
    @PostMapping(value = "/cadastrar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity cadastrarUsuario(@RequestBody @Valid Usuario usuario, BindingResult resultado) {
        if (resultado.hasErrors()) {
            return new ResponseEntity(new Message("erro", "Os dados sobre o usuário não foram informamdo corretamente"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        usuario = us.salvarUsuario(usuario);

        if (usuario != null && usuario.getId() > 0) {
            return new ResponseEntity(usuario, HttpStatus.OK);
        }
        return new ResponseEntity(new Message("erro", "Não foi possível inserir o usuário no banco de dados"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    // URL: http://localhost:8080/usuario/deletar

    @DeleteMapping(value = "/deletar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity removerUsuario(@RequestBody @Valid Usuario usuario, BindingResult resultado) {
        ResponseEntity resposta = null;
        if (resultado.hasErrors()) {
            return new ResponseEntity(new Message("erro", "Os dados sobre o usuário não foram informados corretamente"), HttpStatus.BAD_REQUEST);
        }
        
        if(us.removerUsuario(usuario)){
            return new ResponseEntity(new Message("sucesso", "usuário removido com sucesso"), HttpStatus.OK);
        }

        return new ResponseEntity(new Message("erro", "Não foi possível inserir o usuário no banco de dados"), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    // URL: http://localhost:8080/usuario/atualizar
    @PutMapping(value = "/atualizar", produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
    public ResponseEntity atualizarUsuario(@RequestBody @Valid Usuario usuario, BindingResult resultado) {
        if (resultado.hasErrors()) {
            return new ResponseEntity(new Message("erro", "Os dados sobre o usuário não foram informados corretamente"), HttpStatus.BAD_REQUEST);
        }
        usuario = us.salvarUsuario(usuario);

        if (usuario != null && usuario.getId() > 0) {
            return new ResponseEntity(usuario, HttpStatus.OK);
        }
        return new ResponseEntity(new Message("erro", "Não foi possível inserir o link informado no banco de dados"), HttpStatus.NOT_ACCEPTABLE);
    }
}
