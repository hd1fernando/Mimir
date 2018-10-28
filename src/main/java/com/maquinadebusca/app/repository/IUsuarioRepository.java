package com.maquinadebusca.app.repository;

import com.maquinadebusca.app.model.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long>{
    @Override
    List<Usuario> findAll();
    
    Usuario findById(long id);
    
    @Override
    Usuario save(Usuario usuario);
    
    @Override
    void deleteById(Long id);
        
    List<Usuario> findByUsuarioIgnoreCaseContaining(String nome);
}
