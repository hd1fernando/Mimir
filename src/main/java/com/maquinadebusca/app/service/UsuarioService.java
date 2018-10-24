package com.maquinadebusca.app.service;

import com.maquinadebusca.app.repository.IUsuarioRepository;
import com.maquinadebusca.app.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;

public class UsuarioService {

    @Autowired
    IUsuarioRepository ur;

    public Usuario salvarUsuario(Usuario usuario) {
        Usuario u = null;
        try {
            u = ur.save(usuario);
        } catch (Exception e) {
            System.err.println("\n>> Não foi possível salvar o usuário informado no Banco de Dados\n");
            e.printStackTrace();
        }
        return u;
    }
    
    
}
