package com.maquinadebusca.app.repository;

import com.maquinadebusca.app.model.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long>{
    @Override
    List<Usuario> findAll();
    
    Usuario findById(long id);
    
    @Override
    Usuario save(Usuario usuario);
    
    @Override
    void deleteById(Long id);
        
    List<Usuario> findByUsuarioIgnoreCaseContaining(String nome);
    
    @Query(value = "SELECT * FROM usuario ORDER BY usuario", nativeQuery=true)
    List<Usuario> getInLexicalOrder();
}
