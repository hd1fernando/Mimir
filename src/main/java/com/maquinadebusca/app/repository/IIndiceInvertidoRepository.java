package com.maquinadebusca.app.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.maquinadebusca.app.model.IdIndiceInvertido;
import com.maquinadebusca.app.model.IndiceInvertido;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IIndiceInvertidoRepository extends JpaRepository<IndiceInvertido, IdIndiceInvertido> {

    @Query(value = "select  i.* "
            + "from termo_documento t, indice_invertido i, documento d "
            + "where t.id = i.termo_id and "
            + "          i.documento_id = d.id and "
            + "          t.texto = :termoConsulta ", nativeQuery = true)
    List<IndiceInvertido> getEntradasIndiceInvertido(@Param("termoConsulta") String termo);

}
