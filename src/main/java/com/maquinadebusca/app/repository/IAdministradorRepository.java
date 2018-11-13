package com.maquinadebusca.app.repository;

import com.maquinadebusca.app.model.Administrador;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IAdministradorRepository extends JpaRepository<Administrador, Long>{

    @Override
    List<Administrador> findAll();

    Administrador findById(long id);

    @Override
    Administrador save(Administrador administrador);

    void deleteById(Administrador id);

    List<Administrador> findByUsuarioIgnoreCaseContaining(String nome);

    @Query(value = "SELECT * FROM administrador ORDER BY usuario", nativeQuery = true)
    List<Administrador> getInLexicalOrder();
}
