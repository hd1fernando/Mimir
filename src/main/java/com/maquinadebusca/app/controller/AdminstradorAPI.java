package com.maquinadebusca.app.controller;

import com.maquinadebusca.app.Message.Message;
import com.maquinadebusca.app.model.Administrador;
import com.maquinadebusca.app.model.Usuario;
import com.maquinadebusca.app.service.AdministradorService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminstradorAPI {

    @Autowired
    AdministradorService as;

    // URL: http://localhost:8080/admin/cadastrar
    @PostMapping(value = "/cadastrar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity cadastrarAdministrador(@RequestBody @Valid Administrador administrador, BindingResult resultado) {
        if (resultado.hasErrors()) {
            return new ResponseEntity(new Message("erro", "Os dados sobre o usuário não foram informamdo corretamente"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (administrador.getSenha() == null || administrador.getSenha().equals("")) {
            return new ResponseEntity(new Message("erro", "O campo de senha deve ser informado"), HttpStatus.BAD_REQUEST);
        }

        if (administrador != null) {
            administrador = as.salvarAdministrador(administrador);
            return new ResponseEntity(administrador, HttpStatus.OK);
        }
        return new ResponseEntity(new Message("erro", "Não foi possível inserir o usuário no banco de dados"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // URL: http://localhost:8080/admin/deletar/{id}
    @DeleteMapping(value = "/deletar/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity removerAdministrador(@PathVariable(value = "id") Long id) {
        if (id != null && id <= 0) {
            return new ResponseEntity(new Message("erro", "os dados sobre o usuário não foram informado corretamente"), HttpStatus.BAD_REQUEST);
        }

        if (as.removerAdministrador(id)) {
            return new ResponseEntity(new Message("sucesso", "usuário removido com sucesso"), HttpStatus.OK);
        }
        return new ResponseEntity(new Message("erro", "Não foi possível remover o usuário."), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // URL: http://localhost:8080/admin/atualizar
    @PutMapping(value = "/atualizar", produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
    public ResponseEntity atualizarUsuario(@RequestBody @Valid Administrador usuario, BindingResult resultado) {
        if (resultado.hasErrors()) {
            return new ResponseEntity(new Message("erro", "Os dados sobre o usuário não foram informados corretamente"), HttpStatus.BAD_REQUEST);
        }
        if (usuario != null && usuario.getId() > 0) {
            usuario = as.salvarAdministrador(usuario);
            return new ResponseEntity(usuario, HttpStatus.OK);
        }
        return new ResponseEntity(new Message("erro", "Não foi possível inserir o link informado no banco de dados"), HttpStatus.NOT_ACCEPTABLE);
    }

    // URL: http://localhost:8080/admin/ordemAlfabetica/admin
    @GetMapping(value = "/ordemAlfabetica/admin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity listarAdminOrdemAlfabetica() {
        List<Administrador> listaOrdenada = as.listarAdminOrdemAlfabetica();
        if (listaOrdenada.isEmpty() || listaOrdenada == null) {
            return new ResponseEntity(new Message("erro", "não existe nenhum usuário cadastrado no banco de dados"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(listaOrdenada, HttpStatus.OK);
    }

    // URL: http://localhost:8080/admin/ordemAlfabetica/usuario
    @GetMapping(value = "/ordemAlfabetica/usuario", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity listarEmOrdemAlfabetica() {
        List<Usuario> listaOrdenada = as.listarEmOrdemAlfabetica();
        if (listaOrdenada.isEmpty() || listaOrdenada == null) {
            return new ResponseEntity(new Message("erro", "não existe nenhum usuário cadastrado no banco de dados"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(listaOrdenada, HttpStatus.OK);
    }

    // Request for: http://localhost:8080/admin/encontrar/usuario/{nome}
    @GetMapping(value = "/encontrar/usuario/{nome}", params = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity encontrarUsuario(@PathVariable(value = "nome") String nome) {
        List<Usuario> usuarioEncontrado = as.encontarUsuarioNome(nome);
        if (usuarioEncontrado == null || usuarioEncontrado.isEmpty()) {
            return new ResponseEntity(new Message("erro", "o usuário informado não existe"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(usuarioEncontrado, HttpStatus.OK);
    }

    // Request for: http://localhost:8080/admin/encontrar/admin/{nome}
    @GetMapping(value = "/encontrar/admin/{nome}", params = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity encontrarAdmin(@PathVariable(value = "nome") String nome) {
        List<Usuario> usuarioEncontrado = as.encontarUsuarioNome(nome);
        if (usuarioEncontrado == null || usuarioEncontrado.isEmpty()) {
            return new ResponseEntity(new Message("erro", "o usuário informado não existe"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(usuarioEncontrado, HttpStatus.OK);
    }
}
