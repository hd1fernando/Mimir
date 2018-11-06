/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maquinadebusca.app.repository;

import com.maquinadebusca.app.model.Link;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author fernando
 */
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
}
