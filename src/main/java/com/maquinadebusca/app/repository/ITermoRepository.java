package com.maquinadebusca.app.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.maquinadebusca.app.model.Termo;

public interface ITermoRepository extends JpaRepository<Termo, Long> {

    @Override
    List<Termo> findAll();

    Termo findById(long id);

    @Override
    Termo save(Termo termo);

}
