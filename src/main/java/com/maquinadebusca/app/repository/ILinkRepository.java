/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maquinadebusca.app.repository;

import com.maquinadebusca.app.model.Link;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author fernando
 */
public interface ILinkRepository extends JpaRepository<Link, Long> {

    List<String> findByUrl(String nome);

    @Override
    List<Link> findAll();

    Link findById(long id);
}
