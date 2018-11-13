package com.maquinadebusca.app.service;

import com.maquinadebusca.app.model.Administrador;
import com.maquinadebusca.app.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.maquinadebusca.app.repository.IAdministradorRepository;
import com.maquinadebusca.app.repository.IUsuarioRepository;
import java.util.List;

@Service
public class AdministradorService {

    @Autowired
    IAdministradorRepository ar;

    @Autowired
    IUsuarioRepository ur;

    public Administrador salvarAdministrador(Administrador administrador) {
        Administrador a = null;
        try {
            a = ar.save(administrador);
        } catch (Exception e) {
            System.err.println("\n>> Não foi possível salvar o usuário informado no Banco de Dados\n");
            e.printStackTrace();
        }
        return a;
    }

    public Boolean removerAdministrador(Administrador administrador) {
        try {
            ar.delete(administrador);
            return true;
        } catch (Exception ex) {
            System.err.println("\n>> Não foi possível deletar o usuario informado no Banco de Dados\n");
            ex.printStackTrace();
        }
        return false;
    }

    public Boolean removerAdministrador(long id) {
        try {
            ar.deleteById(id);
            return true;
        } catch (Exception ex) {
            System.err.println("\n>> Não foi possível deletar o usuário informado no Banco de Dados\n");
            ex.printStackTrace();
        }
        return false;
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

    public List<Administrador> encontarAdminNome(String nome) {
        return ar.findByUsuarioIgnoreCaseContaining(nome);
    }

    public List<Usuario> listarEmOrdemAlfabetica() {
        return ur.getInLexicalOrder();
    }

    public List<Administrador> listarAdminOrdemAlfabetica() {
        return ar.getInLexicalOrder();
    }

}
