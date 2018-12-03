package com.maquinadebusca.app.controller;

import com.maquinadebusca.app.Message.Message;
import com.maquinadebusca.app.model.Consulta;
import com.maquinadebusca.app.service.ProcessadorConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/processador")
public class ProcessadorConsulta {

    @Autowired
    ProcessadorConsultaService pcs;

    // URL: http://localhost:8080/processador/consulta/{consultaDoUsuario}
    @GetMapping(value = "/consulta/{consultaDoUsuario}", produces = MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE)
    public ResponseEntity consultar(@PathVariable("consultaDoUsuario") String textoConsulta) {
        Consulta consulta = pcs.processarConsulta(textoConsulta);
        if (!consulta.getRanking().isEmpty()) {
            return new ResponseEntity(consulta, HttpStatus.OK);
        }
        return new ResponseEntity(new Message("erro", "o índice invertido não pode ser criado"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
