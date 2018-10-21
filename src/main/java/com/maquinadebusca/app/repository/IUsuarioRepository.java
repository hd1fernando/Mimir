/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maquinadebusca.app.repository;

import com.maquinadebusca.app.usuario.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author fernando
 */
public interface IUsuarioRepository extends JpaRepository<Usuario, Long>{
    @Override
    List<Usuario> findAll();
    
    Usuario findById(long id);
    
    @Override
    Usuario save(Usuario usuario);
    
    @Override
    void deleteById(Long id);
    
    List<Usuario> findByPermissao(String nome);
    
}
