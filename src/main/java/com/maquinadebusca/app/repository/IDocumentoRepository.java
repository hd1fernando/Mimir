package com.maquinadebusca.app.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.maquinadebusca.app.model.Documento;

public interface IDocumentoRepository extends JpaRepository<Documento, Long> {

    List<String> findByUrl(String nome);

//    Documento saveOrUpdate(Documento documento);
    
    @Override
    List<Documento> findAll();

    Documento findById(long id);
    
    
}
