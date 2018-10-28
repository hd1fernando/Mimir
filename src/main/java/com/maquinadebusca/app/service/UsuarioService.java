package com.maquinadebusca.app.service;

import com.maquinadebusca.app.repository.IUsuarioRepository;
import com.maquinadebusca.app.model.Usuario;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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
    
    public Boolean removerUsuario(Usuario usuario) {
        try {
            ur.delete(usuario);
            return true;
        } catch (Exception ex) {
            System.err.println("\n>> Não foi possível deletar o usuario informado no Banco de Dados\n");
            ex.printStackTrace();
        }
        return false;
    }
    
    public Boolean removerUsuario(long id) {
        try {
            ur.deleteById(id);
            return true;
        } catch (Exception ex) {
            System.err.println("\n>> Não foi possível deletar o usuário informado no Banco de Dados\n");
            ex.printStackTrace();
        }
        return false;
    }    
    
    public List<Usuario> encontarUsuarioNome(String nome) {
        return ur.findByUsuarioIgnoreCaseContaining(nome);
    }
    
}
