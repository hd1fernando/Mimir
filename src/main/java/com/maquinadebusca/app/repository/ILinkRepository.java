package com.maquinadebusca.app.repository;

import com.maquinadebusca.app.model.Link;
import java.time.LocalDateTime;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ILinkRepository extends JpaRepository<Link, Long> {

    List<String> findByUrl(String nome);

    @Override
    List<Link> findAll();

    Link findById(long id);

    @Override
    Link save(Link link);

    @Override
    void deleteById(Long id);

    @Query(value = "SELECT * FROM link WHERE url LIKE %?1%", nativeQuery = true)
    List<Link> findByUrlName(String url);

    @Query(value = "SELECT * FROM link ORDER BY url", nativeQuery = true)
    List<Link> getInLexicalOrder();

    @Query(value = "SELECT * FROM link", nativeQuery = true)
    public Slice<Link> getPage(Pageable pageable);

    @Query(value = "SELECT * FROM link WHERE id BETWEEN ?1 AND ?2", nativeQuery = true)
    List<Link> findLinkByIdRange(Long id1, Long id2);

    @Query(value = "SELECT COUNT(*) FROM link WHERE id between :identificador1 and :identificador2", nativeQuery = true)
    Long countLinkByIdRange(@Param("identificador1") Long id1, @Param("identificador2") Long id2);

    @Query(value = "select * from link where ultima_coleta between :data1 and :data2", nativeQuery = true)
    LocalDateTime findByTime(@Param("data1") LocalDateTime data1, @Param("data2") LocalDateTime data2);

    @Transactional
    @Modifying
    @Query(value = "UPDATE link l SET l.ultima_coleta = :data WHERE l.url LIKE CONCAT('%',:host,'%')", nativeQuery = true)
    int updateLastCrawlingDate(@Param("data") LocalDateTime ultimaColeta, @Param("host") String nomeHost);
}
