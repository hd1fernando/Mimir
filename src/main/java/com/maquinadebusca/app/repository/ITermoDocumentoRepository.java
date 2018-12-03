package com.maquinadebusca.app.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.maquinadebusca.app.model.TermoDocumento;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ITermoDocumentoRepository extends JpaRepository<TermoDocumento, Long> {

    @Override
    List<TermoDocumento> findAll();

    TermoDocumento findById(long id);

    @Override
    TermoDocumento save(TermoDocumento termo);

    @Query(value = "select log (2, count(distinct d.id)/t.n) "
            + "from termo_documento t, documento d "
            + "where t.texto =:termo", nativeQuery = true)
    public double getIdf(@Param("termo") String termo);

}
