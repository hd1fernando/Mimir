package com.maquinadebusca.app.repository;

import com.maquinadebusca.app.model.Host;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IHostRepository extends JpaRepository<Host, Long> {

    @Override
    List<Host> findAll();

    List<String> findByHost(String name);

    Host findById(long id);

    List<Host> findByHostIgnoreCaseContaining(String host);

    @Query(value = "SELECT * FROM host ORDER BY host", nativeQuery = true)
    List<Host> getInLexicalOrder();

}
